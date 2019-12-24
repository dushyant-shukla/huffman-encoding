package com.dushyant.huffman.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.dushyant.huffman.exception.HuffmanException;

public class HuffmanUtility {

    public static final byte[] MAGIC_BYTES = new byte[]{72, 70};

    public static final int BYTES_PER_HUFFMAN_NODE = 1 + Integer.BYTES; // 1 byte for the 8 bit pattern + 4 bytes for the frequency

    public static final String DECOMPRESSED_FILE = System.getProperty("java.io.tmpdir") + File.separator + "huffman" + File.separator + "decompressed-file";

    public static final String COMPRESSED_FILE = System.getProperty("java.io.tmpdir") + File.separator + "huffman" + File.separator + "compressed-file";

    private HuffmanUtility() {
    }

    public static byte[] readAllBytes(String filePath) {
        validateFilePath(filePath);
        try {
            byte[] input = Files.readAllBytes(Paths.get(filePath));
            if (input.length != 0) {
                return input;
            }
            throw new HuffmanException("Received an empty file: " + filePath);
        } catch (IOException ioException) {
            throw new HuffmanException("An error occurred while reading the file: " + filePath, ioException);
        }
    }

    public static void writeAllBytes(byte[] bytes, String filePath) {
        validateFilePath(filePath);
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(bytes);
        } catch (IOException ioException) {
            throw new HuffmanException("An error occurred while writing to the file: " + filePath, ioException);
        }
    }

    public static void writeAllBytes(Byte[] bytes, String filePath) {
        byte[] data = new byte[bytes.length];
        for (int index = 0; index < bytes.length; ++index) {
            data[index] = bytes[index].byteValue();
        }
        writeAllBytes(data, filePath);
    }

    private static void validateFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new HuffmanException("Invalid file path.");
        }
    }
}