package com.dushyant.huffman.model;

import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

    private PriorityQueue<HuffmanNode> tree;

    public HuffmanTree(PriorityQueue<HuffmanNode> tree) {
        this.tree = tree;
    }

    public HuffmanTree(HuffmanTree otherTree) {
        this.tree = otherTree.tree;
    }

    public HuffmanTree(FrequencyTable symbolFrequencies) {
        PriorityQueue<HuffmanNode> huffmanNodes = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : symbolFrequencies.getFrequencyTable().entrySet()) {
            HuffmanNode newNode = new HuffmanLeafNode(entry.getKey(), entry.getValue());
            huffmanNodes.add(newNode);
        }

        while (huffmanNodes.size() > 1) {
            HuffmanNode firstNode = huffmanNodes.poll();
            HuffmanNode secondNode = huffmanNodes.poll();
            HuffmanInternalNode internalNode = new HuffmanInternalNode();
            internalNode.setLeftChild(firstNode);
            internalNode.setRightChild(secondNode);
            huffmanNodes.add(internalNode);
        }
        this.tree = huffmanNodes;
    }

    public PriorityQueue<HuffmanNode> getTree() {
        return this.tree;
    }

}
