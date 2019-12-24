package com.dushyant.huffman.model;

import java.util.HashMap;
import java.util.Map;

public class FrequencyTable {

    private Map<Byte, Integer> frequencies;

    public FrequencyTable() {
        this.frequencies = new HashMap<>();
    }

    public void incrementCount(byte newByte) {
        if (frequencies.containsKey(newByte)) {
            frequencies.computeIfPresent(newByte, (theByte, theCount) -> ++theCount);
        } else {
            frequencies.put(newByte, 1);
        }
    }

    public void addSymbol(byte newByte, int frequency) {
        this.frequencies.putIfAbsent(newByte, frequency);
    }

    public Map<Byte, Integer> getFrequencyTable() {
        return this.frequencies;
    }
}
