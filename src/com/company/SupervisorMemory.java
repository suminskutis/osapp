package com.company;

/**
 * Created by suminskutis on 4/24/2017.
 */
public class SupervisorMemory {

    static Word[] memory;
    static final int SUPERVISOR_MEMORY_SIZE = 1024;

    public SupervisorMemory() {
        memory = new Word[SUPERVISOR_MEMORY_SIZE];
    }


    public Word read(int address) {
        return memory[address];
    }

    public void write(Word word, int address) {
        Word temp = new Word();
        temp.setValue(word.getValue());
        memory[address] = temp;
    }

    public void writeString(String string, int address) {
        memory[address].setValue(string);
    }

    public static Word[] getMemory() {
        return memory;
    }

    public static void setMemory(Word[] memory) {
        SupervisorMemory.memory = memory;
    }

    public static void printSupervisorMemory(){
        Word word;
        for (int i = 0; i < SUPERVISOR_MEMORY_SIZE; i++){
            word = memory[i];
            if(word == null){
                System.out.println("null");
            }else{
                System.out.println(word.getValue() + " ");
            }
        }
    }

    public void initMemory(){
        Word word = new Word();
        for(int i = 0; i < memory.length; i++){
            memory[i] = word;
        }


    }

    public static int getSize(){
        int size = 0;
        String element = memory[size].getValue();
        while(!(element.equalsIgnoreCase("HALT"))){
            element = memory[size].getValue();
            size++;
        }

        return size;
    }
}
