package com.company;


public class VirtualMemory {

    Word[] memory;

    public VirtualMemory(int size) {
        memory = new Word[size];
    }

    public Word[] getMemory(){
        return memory;
    }

    public Word read(int address) {
        return memory[address];
    }
    public String readString(int address){
        String value = memory[address].getValue();
        return value;
    }
    public void write(Word word, int address) {
        Word temp = new Word();
        temp.setValue(word.getValue());
        memory[address] = temp;
    }

    public void writeString(String string, int address) {
        memory[address].setValue(string);
    }

    public void writeStringToWord(String string, int address){
        memory[address - 1].setValue("null");
        memory[address].setValue(string);
    }
}
