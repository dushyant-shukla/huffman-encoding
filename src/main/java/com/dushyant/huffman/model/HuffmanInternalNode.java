package com.dushyant.huffman.model;

public class HuffmanInternalNode implements HuffmanNode {

	private HuffmanNode leftChild;

	private HuffmanNode rightChild;

	public HuffmanNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(HuffmanNode leftChild) {
		this.leftChild = leftChild;
	}

	public HuffmanNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(HuffmanNode rightChild) {
		this.rightChild = rightChild;
	}

	@Override
	public int frequency() {
		return this.leftChild.frequency() + this.rightChild.frequency();
	}
}
