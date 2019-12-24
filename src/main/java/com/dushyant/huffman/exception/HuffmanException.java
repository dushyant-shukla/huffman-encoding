package com.dushyant.huffman.exception;

public class HuffmanException extends RuntimeException {

    public HuffmanException() {
        super();
    }

    public HuffmanException(String message) {
        super(message);
    }

    public HuffmanException(String message, Throwable cause) {
        super(message, cause);
    }

    public HuffmanException(Throwable cause) {
        super(cause);
    }
}
