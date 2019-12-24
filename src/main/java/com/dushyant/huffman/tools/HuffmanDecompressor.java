package com.dushyant.huffman.tools;

import static com.dushyant.huffman.utils.HuffmanUtility.BYTES_PER_HUFFMAN_NODE;
import static com.dushyant.huffman.utils.HuffmanUtility.MAGIC_BYTES;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Logger;

import com.dushyant.huffman.exception.HuffmanException;
import com.dushyant.huffman.model.*;

/**
 * The {@code HuffmanDecompressor} class provides method for expanding a Huffman-compressed data.
 *
 * @author  Dushyant Shukla
 * @version 1.0
 */
public class HuffmanDecompressor {

    private static final Logger LOGGER = Logger.getLogger(HuffmanDecompressor.class.getName());

    /**
     * The method expands an already Huffman-compressed data into its original form. Throws {@link HuffmanException} if the check for
     * MAGIC BYTES fails. The compressed data must have the below structure:
     *          ------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *         |                                                                                                                                                                 |
     *        | --------------    ----------------------------------------------------------    ----------------------------------------------------------------------------    |    -------------------
     *       | | MAGIC BYTES | + | NUMBER OF DIFFERENT 8 BIT PATTERNS (HUFFMAN LEAF NODES) | + | HUFFMAN LEAF NODES (8 BIT PATTERN FOLLOWED BY CORRESPONDING FREQUENCY)    |   |  + | HUFFMAN ENCODING |
     *      |  --------------    ----------------------------------------------------------    ----------------------------------------------------------------------------   |     -------------------
     *     |     2 BYTES                              4 BYTES                                            NUMBER OF HUFFMAN LEAF NODES * (1 + 4) BYTES PER NODE               |
     *    -----------------------------------------------------------------------------   HEADER   --------------------------------------------------------------------------
     *
     * @param byte[] compressedData
     * @return Byte[] the decompressed data
     */
    public Byte[] decompress(byte[] compressedData) {
        validateMagicBytes(compressedData);

        // Extracting the number of Huffman leaf nodes from the compressed data. The next four bytes after the MAGIC BYTES contain the number of Huffman leaf nodes.
        int numberOfHuffmanLeafNodes = ByteBuffer.wrap(compressedData, MAGIC_BYTES.length, Integer.BYTES).getInt();

        FrequencyTable huffmanNodeFrequencies = new FrequencyTable();
        for (int index = MAGIC_BYTES.length + Integer.BYTES; index < MAGIC_BYTES.length + Integer.BYTES + (numberOfHuffmanLeafNodes * BYTES_PER_HUFFMAN_NODE); index = index + BYTES_PER_HUFFMAN_NODE) {
            byte bitPattern = compressedData[index];
            int frequency = ByteBuffer.wrap(compressedData, index + 1, Integer.BYTES).getInt();
            huffmanNodeFrequencies.addSymbol(bitPattern, frequency);
        }

        HuffmanTree huffmanTree = new HuffmanTree(huffmanNodeFrequencies);

        // Determining the total size(bytes) of the header.
        int sizeofHeadersInBytes = MAGIC_BYTES.length + Integer.BYTES + (BYTES_PER_HUFFMAN_NODE * numberOfHuffmanLeafNodes);

        /*
            Extracting the Huffman encoding from the compressed data.
            The compressed data has two parts: headers(magic bytes + bytes storing the number different 8 bit patterns + bytes storing the bit patterns and corresponding frequencies) and actual huffman encoded data.
            Here total size of headers (bytes) acts as an offset for the Huffman encoded portion in the compressed data.
         */
        BitSet huffmanEncoding = BitSet.valueOf(ByteBuffer.wrap(compressedData, sizeofHeadersInBytes, compressedData.length - sizeofHeadersInBytes));

        List<Byte> decompressedData = decode(huffmanEncoding, huffmanTree);

        return decompressedData.toArray(new Byte[decompressedData.size()]);
    }

    /**
     * The method decodes Huffman encoded data using the passed in Huffman Tree.
     *
     * @param BitSet, the huffman encoded original data
     * @param {@code HuffmanTree}
     * @return List<Byte>, the decoded data
     */
    private List<Byte> decode(BitSet huffmanEncoding, HuffmanTree huffmanTree) {
        HuffmanNode rootNode = huffmanTree.getTree().poll();
        HuffmanNode node = rootNode;
        List<Byte> decompressedData = new ArrayList<>();
        for (int index = 0; index < huffmanEncoding.length(); ++index) {
            if (node instanceof HuffmanInternalNode) {
                HuffmanInternalNode internalNode = (HuffmanInternalNode) node;
                boolean bit = huffmanEncoding.get(index);
                if (bit) {
                    node = internalNode.getRightChild();
                } else {
                    node = internalNode.getLeftChild();
                }
            }
            if (node instanceof HuffmanLeafNode) {
                HuffmanLeafNode leafNode = (HuffmanLeafNode) node;
                byte symbol = leafNode.getBitPattern();
                decompressedData.add(symbol);
                node = rootNode;
            }
        }
        return decompressedData;
    }

    private void validateMagicBytes(byte[] bytes) {
        for (int index = 0; index < MAGIC_BYTES.length; ++index) {
            if (bytes[index] != MAGIC_BYTES[index]) {
                throw new HuffmanException("Check for MAGIC CODE failed. Please provide a valid compressed file.");
            }
        }
    }

}
