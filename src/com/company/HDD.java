package com.company;


public class HDD {

    static Word[] memory;
    static final int HDD_SIZE = 1024;
    static int lastWordIndex = 0;

    public HDD(){
        memory = new Word[HDD_SIZE];
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

    public void write(Word word) {
        Word temp = new Word();
        temp.setValue(word.getValue());
        memory[lastWordIndex] = temp;
        lastWordIndex++;
    }

    public void writeString(String string) {
        memory[lastWordIndex].setValue(string);
        lastWordIndex++;
    }

    public static int getLastWordIndex() {
        return lastWordIndex;
    }

    public static void setLastWordIndex(int lastWordIndex) {
        HDD.lastWordIndex = lastWordIndex;
    }

    public static void printHDD(){
        Word word;
        for (int i = 0; i < HDD_SIZE; i++){
            word = memory[i];
            if(word == null){
                System.out.println("null");
            }else{
                System.out.println(word.getValue() + " ");
            }
        }
    }

}
