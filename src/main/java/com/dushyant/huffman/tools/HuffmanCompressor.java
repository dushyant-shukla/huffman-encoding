package com.dushyant.huffman.tools;

import static com.dushyant.huffman.utils.HuffmanUtility.BYTES_PER_HUFFMAN_NODE;
import static com.dushyant.huffman.utils.HuffmanUtility.MAGIC_BYTES;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.dushyant.huffman.exception.HuffmanException;
import com.dushyant.huffman.model.*;

/**
 * The {@code HuffmanCompressor} class provides method for compressing data using Huffman encoding.
 *
 * @author  Dushyant Shukla
 * @version 1.0
 */
public class HuffmanCompressor {

    private static final Logger LOGGER = Logger.getLogger(HuffmanCompressor.class.getName());

    /**
     * The method compresses data using Huffman encoding. The method adds certain header to the Huffman encoded data. The final
     * compressed data has below format:
     *
     *          ------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *         |                                                                                                                                                                 |
     *        | --------------    ----------------------------------------------------------    ----------------------------------------------------------------------------    |    -------------------
     *       | | MAGIC BYTES | + | NUMBER OF DIFFERENT 8 BIT PATTERNS (HUFFMAN LEAF NODES) | + | HUFFMAN LEAF NODES (8 BIT PATTERNS FOLLOWED BY CORRESPONDING FREQUENCIES) |   |  + | HUFFMAN ENCODING |
     *      |  --------------    ----------------------------------------------------------    ----------------------------------------------------------------------------   |     -------------------
     *     |     2 BYTES                              4 BYTES                                            NUMBER OF HUFFMAN LEAF NODES * (1 + 4) BYTES PER NODE               |
     *    -----------------------------------------------------------------------------   HEADER   --------------------------------------------------------------------------
     *
     * @param byte[], original data
     * @return byte[], compressed data
     */
    public byte[] compress(byte[] originalData) {

        FrequencyTable bitPatternFrequencies = new FrequencyTable();
        for (byte bitPattern : originalData) {
            bitPatternFrequencies.incrementCount(bitPattern);
        }

        HuffmanTree huffmanTree = new HuffmanTree(bitPatternFrequencies);

        Map<Byte, String> huffmanCodes = generateHuffmanCodes(huffmanTree);

        BitSet huffmanEncoding = encode(originalData, huffmanCodes);

        return addHeadersToHuffmanEncoding(huffmanEncoding, bitPatternFrequencies);
    }

    /**
     * The method created Huffman encoding for the original data.
     *
     * @param byte[], the original data
     * @param Map<Byte, String>, generated Huffman codes for different bit patterns
     * @return BitSet holding huffman encoding for the original data
     */
    private BitSet encode(byte[] originalData, Map<Byte, String> huffmanCodes) {
        BitSet huffmanEncoding = new BitSet();
        int bitIndex = 0;
        for (byte bitPattern : originalData) {
            String huffmanCode = huffmanCodes.get(bitPattern);
            for (char bitValue : huffmanCode.toCharArray()) {
                if (bitValue == '1') {
                    huffmanEncoding.set(bitIndex++);
                } else {
                    huffmanEncoding.clear(bitIndex++);
                }
            }
        }
        return huffmanEncoding;
    }

    /**
     * The method generates the Huffman codes for all the different bit patterns in the original data using the
     * passed in Huffman Tree.
     *
     * @param {@code HuffmanTree}
     * @return Huffman codes for the bit patterns using the passed in Huffman Tree.
     */
    private Map<Byte, String> generateHuffmanCodes(HuffmanTree huffmanTree) {
        Map<Byte, String> huffmanCodes = new HashMap<>();
        HuffmanNode rootNode = huffmanTree.getTree().poll();
        createHuffmanCodes(rootNode, "", huffmanCodes);
        return huffmanCodes;
    }

    /**
     * The method recursively creates Huffman codes and populates the Map<Character,
     * String> huffmanCodes for the different 8-bit patterns in the input.
     *
     * @param {@code HuffmanNode}
     * @param String, huffman code
     * @param Map<Byte, String>, map to hold Huffman codes for all the symbols in the input
     */
    private void createHuffmanCodes(HuffmanNode node, String huffmanCode, Map<Byte, String> huffmanCodes) {
        if (node instanceof HuffmanInternalNode) {
            createHuffmanCodes(((HuffmanInternalNode) node).getLeftChild(), huffmanCode + '0', huffmanCodes);
            createHuffmanCodes(((HuffmanInternalNode) node).getRightChild(), huffmanCode + '1', huffmanCodes);
        } else {
            HuffmanLeafNode leafNode = (HuffmanLeafNode) node;
            huffmanCodes.put(leafNode.getBitPattern(), huffmanCode);
        }
    }

    private byte[] addHeadersToHuffmanEncoding(BitSet huffmanEncoding, FrequencyTable bitPatternFrequencies) {
        ByteBuffer compressedData = ByteBuffer.allocate(calculateSizeAfterCompression(huffmanEncoding, bitPatternFrequencies));
        compressedData.put(MAGIC_BYTES);
        compressedData.putInt(bitPatternFrequencies.getFrequencyTable().size());
        for (Map.Entry<Byte, Integer> entry : bitPatternFrequencies.getFrequencyTable().entrySet()) {
            compressedData.put(entry.getKey()); // bit pattern
            compressedData.putInt(entry.getValue()); // number of occurrences of the bit pattern
        }

        compressedData.put(huffmanEncoding.toByteArray());
        return compressedData.array();
    }

    /**
     * The method calculates the total size of the compressed file in bytes using the below formula:
     * Total size = number of bytes required to store the magic number
     *                                      +
     *              number of bytes required for storing the number of different 8 bit patterns in the original data
     *                                      +
     *              total number of bytes required to store the huffman nodes (bit pattern + corresponding frequency)
     *                                      +
     *              total bytes required to store the huffman encoding
     *
     * @param BitSet, the Huffman Encoding
     * @param {@code FrequencyTable}, the frequencies for different 8 bit patterns in the original data
     * @return int, size of compressed file in bytes
     */
    private int calculateSizeAfterCompression(BitSet huffmanEncoding, FrequencyTable bitPatternFrequencies) {
        return MAGIC_BYTES.length + Integer.BYTES + (BYTES_PER_HUFFMAN_NODE * bitPatternFrequencies.getFrequencyTable().size()) + huffmanEncoding.toByteArray().length;
    }
}
