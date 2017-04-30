package com.company;


public class OutputDevice {

    public static void printString(Word word){
        System.out.println(word.getValue());
    }

    public static void printInt(Word word){
        int output = Integer.parseInt(word.getValue(), 16);
        System.out.println(output);
    }
}
