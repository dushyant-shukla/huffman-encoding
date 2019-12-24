package com.dushyant.huffman.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.dushyant.huffman.exception.HuffmanException;

public class HuffmanDecompressorTest {

    private HuffmanDecompressor huffmanDecompressor;

    private byte[] compressedData;

    private byte[] compressedDataWithoutMagicBytes;

    @Before
    public void setUp() throws Exception {
        huffmanDecompressor = new HuffmanDecompressor();

        Path path = Paths.get(getClass().getClassLoader().getResource("sample-input-decompression/compressed-file").toURI());
        compressedData = Files.readAllBytes(path);

        path = Paths.get(getClass().getClassLoader().getResource("sample-input-decompression/compressed-data-without-magic-bytes").toURI());
        compressedDataWithoutMagicBytes = Files.readAllBytes(path);
    }

    @Test
    public void testDecompress() {
        Byte[] decompressedData = huffmanDecompressor.decompress(compressedData);
        assertEquals("Size of decompressed data in bytes", 3529, decompressedData.length);
        assertTrue(decompressedData.length > compressedData.length);
    }

    @Test(expected = HuffmanException.class)
    public void testDecompressWithInvalidMagicBytes() {
        huffmanDecompressor.decompress(compressedDataWithoutMagicBytes);
    }
}