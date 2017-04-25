package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by suminskutis on 4/23/2017.
 */
public class InputDevice {

    private static BufferedReader br;
    //private static String string = new String("ABCDEFGHI");

    public static void openFile() throws FileNotFoundException {
        br = new BufferedReader(new FileReader("programa1.txt"));
    }

    public static Word getInputLine() throws IOException {

        Word word = new Word();

        String string;
        if((string = br.readLine()) == null){
            word.setValue("NULL");
        }else {
            word.setValue(string);
        }


        return word;
    }
}
