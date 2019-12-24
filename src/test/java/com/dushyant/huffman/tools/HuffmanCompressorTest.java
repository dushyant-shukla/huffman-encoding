package com.dushyant.huffman.tools;

import static com.dushyant.huffman.utils.HuffmanUtility.BYTES_PER_HUFFMAN_NODE;
import static com.dushyant.huffman.utils.HuffmanUtility.MAGIC_BYTES;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class HuffmanCompressorTest {

    private HuffmanCompressor huffmanCompressor;

    private byte[] inputData;

    @Before
    public void setUp() throws Exception {
        huffmanCompressor = new HuffmanCompressor();
        Path path = Paths.get(getClass().getClassLoader().getResource("sample-input-compression/sample.txt").toURI());
        inputData = Files.readAllBytes(path);
    }

    @Test
    public void testCompress() {
        byte[] compressedData = huffmanCompressor.compress(inputData);
        assertEquals("Size of compressed data", 2200, compressedData.length);
        byte[] actualMagicBytes = ByteBuffer.wrap(compressedData, 0, MAGIC_BYTES.length).array();
        for (int index = 0; index < MAGIC_BYTES.length; ++index) {
            assertEquals("Magic byte", MAGIC_BYTES[index], actualMagicBytes[index]);
        }
        int numberOfHuffmanLeafNodes = ByteBuffer.wrap(compressedData, MAGIC_BYTES.length, Integer.BYTES).getInt();
        assertEquals("Number of different 8 bit patterns", 51, numberOfHuffmanLeafNodes);
        int sizeofHeaderInBytes = MAGIC_BYTES.length + Integer.BYTES + BYTES_PER_HUFFMAN_NODE * numberOfHuffmanLeafNodes;
        assertEquals("Size of header in bytes", 261, sizeofHeaderInBytes);
        int sizeofHuffmanEncodingInBits = BitSet.valueOf(ByteBuffer.wrap(compressedData, sizeofHeaderInBytes, compressedData.length - sizeofHeaderInBytes)).length();
        assertEquals("", 15511, sizeofHuffmanEncodingInBits);
    }
}