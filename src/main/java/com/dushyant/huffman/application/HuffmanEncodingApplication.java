package com.dushyant.huffman.application;

import static com.dushyant.huffman.utils.HuffmanUtility.COMPRESSED_FILE;
import static com.dushyant.huffman.utils.HuffmanUtility.DECOMPRESSED_FILE;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dushyant.huffman.tools.HuffmanCompressor;
import com.dushyant.huffman.tools.HuffmanDecompressor;
import com.dushyant.huffman.utils.HuffmanUtility;

public class HuffmanEncodingApplication {

    private static final Logger LOGGER = Logger.getLogger(HuffmanEncodingApplication.class.getName());

    public static void main(String[] args) {
        try {
            System.out.print("Please create a folder with name 'huffman' under " + System.getProperty("java.io.tmpdir") +
                    ".\nThe program will store the compressed file under " + System.getProperty("java.io.tmpdir") + "/huffman." +
                    "\n\nPress 'ENTER' when ready.");
            System.in.read();
            Scanner scanner = new Scanner(System.in);
            int choice;
            String exit;
            do {
                System.out.print("\nPlease select one of the choices below:\ni.   Press 1 to compress a file\nii.  Press 2 to decompress a file\niii. Press 0 for exit\n\nchoice: ");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        startCompressionRoutine();
                        break;
                    case 2:
                        startDecompressionRoutine();
                        break;
                    case 0:
                    default:
                        System.out.println("\nExiting now...");
                        System.exit(0);

                }
                System.out.print("Want to continue? (yes or no): ");
                exit = scanner.next();
            } while (exit.equalsIgnoreCase("YES"));
            System.out.println("\nExiting now...");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
        }
    }

    private static void startCompressionRoutine() {
        try {
            HuffmanCompressor huffmanCompressor = new HuffmanCompressor();
            System.out.print("\nPlease enter the absolute path for the file you want to compress below:\npath: ");
            Scanner inputScanner = new Scanner(System.in);
            String inputFilePath = inputScanner.nextLine();
            byte[] inputBytes = HuffmanUtility.readAllBytes(inputFilePath.trim());
            byte[] compressedData = huffmanCompressor.compress(inputBytes);
            HuffmanUtility.writeAllBytes(compressedData, COMPRESSED_FILE);
            LOGGER.info("The compressed data has been saved successfully at: " + COMPRESSED_FILE);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
        }
    }

    private static void startDecompressionRoutine() {
        try {
            HuffmanDecompressor huffmanDecompressor = new HuffmanDecompressor();
            System.out.print("\nPlease enter the absolute path for the file you want to decompress below:\npath: ");
            Scanner inputScanner = new Scanner(System.in);
            String compressedFilePath = inputScanner.nextLine();
            byte[] encodedData = HuffmanUtility.readAllBytes(compressedFilePath);
            Byte[] decompressedData = huffmanDecompressor.decompress(encodedData);
            HuffmanUtility.writeAllBytes(decompressedData, DECOMPRESSED_FILE);
            LOGGER.info("The decoded content has been saved successfully at: " + DECOMPRESSED_FILE);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
        }
    }

}
