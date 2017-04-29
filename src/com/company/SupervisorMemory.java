package com.company;

import java.io.BufferedReader;
import java.io.FileReader;

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
    
    public boolean validateProgram()
    {

		boolean DATASEG_found = false;
		boolean INTEGERS_found = false;
		boolean CODESEG_found = false;
		boolean HALT_found = false;
		boolean CMDS_found = false;
		
		int DATASEG_id = 0;
		int CODESEG_id = 0;
		int HALT_id = 0;
		 
		String[] SEGS_Array = {"DATA","CODE"};
		String[] EXITCMDS_Array = {"HALT"};
		String[] CMDS_Array = {"ADD", "SUB", "DIV", "MUL", "PRTS", "PU"};
		int CMDS_Array_Length = CMDS_Array.length; 

//			------------------------    READING FILE INTO ARRAY    ------------------------
		    int linecounter = 0;
		    Word word = memory[0];
//		    System.out.println("duxas: " +word.getValue());
		    
//		    return true;
//			------------------------    DATASEG  VALIDATION    ------------------------
		    if(word.getValue().equals(SEGS_Array[0]) ) //CHECK IF DATASEG is in place
		    {
		    	DATASEG_found = true;
//		    	System.out.println("DATASEG radau");
		    }
		    else return false; // No Data segment found = exit
		    
//			------------------------   DETERMINE PROGRAM LENGTH & HALT pos    ------------------------		    
		    for (int i = 0; i < SUPERVISOR_MEMORY_SIZE; i++){
	            word = memory[i];
	            if(word.getValue().equals(EXITCMDS_Array[0]) ){
//	                System.out.println("bybys");
	                HALT_found = true;
	                HALT_id = i;
	                break;
	            }
//	            linecounter++;
		    }
		    if (HALT_found == false) return false;
		    if(	!(( (word = memory[HALT_id + 1]).getValue() ).equals( (word = new Word()).getValue() )) ) //Assuming that HALT command is last if not = exit
		    {
//		    	System.out.println("bybianosis isivele");
		    	return false;
		    }
//		    System.out.println(HALT_id);
//		    System.out.println((word = memory[linecounter]).getValue());
//			------------------------    CODESEG VALIDATION    ------------------------
		    for(int i = DATASEG_id; i < HALT_id; i++) // Looking for CODESEG and HALT command
		    {
		    	word = memory[i];
		    	if((word.getValue()).equals(SEGS_Array[1])) //CODESEG
		    	{
		    		CODESEG_found = true;
		    		CODESEG_id = i;
//		    		System.out.println("CODESEG radau" + i);
		    	}
		    }
		    if(!CODESEG_found) return false; // No Code segment found = exit
//		    return true;
		   
//		    ------------------------    DATASEG VALUES VALIDATION    ------------------------
		    for(int i = DATASEG_id + 1; i < CODESEG_id; i++) // Looking for DATASEG values
		    {
		    	word = memory[i];
		    	int numeric = Integer.parseInt(word.getValue()); // Throws exception if its not int
		    	int length = word.getValue().length();
//		    	System.out.println("int repo: " + numeric + " ilgis: " + length);
				if(length == 4 && numeric > 0)
				{
					INTEGERS_found = true;
				}
				else return false;	// Found not numeric or zero value = exit	    				
		    }
//			------------------------    CODESEG VALUES VALIDATION    ------------------------		    
		    for(int i = CODESEG_id + 1; i < HALT_id; i++) // Looking for CODESEG Commands
		    {
		    	word = memory[i];
//		    	System.out.println("Comanda radau: " + word.getValue());
		    	boolean CMD_found = false;
//		    	System.out.println("Imu: " + word.getValue());
		    	for(int j = 0; j < CMDS_Array_Length; j++)
		    	{
//		    		System.out.println("Ieskau");
		    		if(word.getValue().contains(CMDS_Array[j]))
		    		{
		    			CMD_found = true;
//		    			System.out.println("Comanda radau: " + word.getValue());
		    		}
		    	}
		    	if(!CMD_found) // Command checked didn't match any accepted commads = exit
		    	{
//		    		System.out.println("NIEKO GERO");
		    		return false;
		    	}
		    }
		    CMDS_found = true;
		    return true; // FILE VALIDATION SUCCESSFUL!
	}

}

