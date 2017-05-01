package com.company;

public class Word {

    private String data;

    public Word() {
        data = "0000";
    }

    public Word(int s) {
        setIntValue(s);
    }

    public Word(String s) {
        setValue(s);
    }

    public int getIntValue() {
        try {
            return Integer.parseInt(data, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getValue() {
        return data.toUpperCase();
    }

    public void setValue(String s) {

        if (s.length() <= 4)
            data = s;
        else
            data = s.substring(0, 4);
    }

    public void setIntValue(int s) {
        data = Integer.toHexString(s);
        if (data.length() > 4)
            data = data.substring(data.length() - 4, data.length());
    }

    public static Word intToWord(int value){
        Word word = new Word();
        word.setIntValue(value);
        return word;
    }


}
