package com.dushyant.huffman.model;

public class HuffmanLeafNode implements HuffmanNode {

    private byte bitPattern;

    private int frequency;

    public HuffmanLeafNode(byte bitPattern, int frequency) {
        this.bitPattern = bitPattern;
        this.frequency = frequency;
    }

    public byte getBitPattern() {
        return bitPattern;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int frequency() {
        return getFrequency();
    }

}
