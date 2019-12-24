package com.dushyant.huffman.model;

public interface HuffmanNode extends Comparable<HuffmanNode> {

    int frequency();

    @Override
    default int compareTo(HuffmanNode otherHuffmanNode) {
        return this.frequency() - otherHuffmanNode.frequency();
    }
}
