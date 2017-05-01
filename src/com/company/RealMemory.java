package com.company;

public class RealMemory {

    static Word[] memory;
    public final int RM_MEMORY_SIZE = 4096;


    public RealMemory() {
        memory = new Word[RM_MEMORY_SIZE];
    }

    public static void write(Word word, int address) {
        Word temp = new Word();
        temp.setValue(word.getValue());
        memory[address] = temp;
    }

}
