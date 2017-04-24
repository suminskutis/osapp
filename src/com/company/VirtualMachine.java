package com.company;

public class VirtualMachine {

    private VirtualCPU virtualCPU;
    private VirtualMemory virtualMemory;
    private int index;


    public static final int VM_MEMORY_SIZE = 256;
    public static final int MAX_INT = 65535;

    final static int DATA_START = 0;
    final static int DATA_SIZE = 112;

    final static int PROGRAM_START = 112;
    final static int PROGRAM_SIZE = 112;

    final static int STACK_START = 224;
    final static int STACK_SIZE = 32;


    // Default constructor
    public VirtualMachine(/*int index*/){
        this.virtualCPU = new VirtualCPU();
        this.virtualMemory = new VirtualMemory(VM_MEMORY_SIZE);
        //this.index = index;
    }

    public VirtualMemory getVirtualMemory(){
        return virtualMemory;
    }
    public VirtualCPU getVirtualCPU(){
        return virtualCPU;
    }
    public void printMemory() {
        Word word;
        for(int i = 0; i < VM_MEMORY_SIZE; i++){
            word = virtualMemory.getMemory()[i];
            if(word == null){
                System.out.println("null");
            }else{
            System.out.println(word.getValue() + " ");
            }
        }
    }


    //~~~~~~~~~~~~~~~~COMMANDS~~~~~~~~~~~~~~~~
    public void ADD(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = aN + bN;
     /*   if(result > MAX_INT){ TODO uzdet tikrinima
            RealMachine.setCF();
        }*/

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1  );
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
    }

    public void SUB(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = bN - aN;
       /* if(result < 0){ TODO uzdet tikrinima
            RealMachine.setOF();
        }*/

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
    }

    public void MUL(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = aN * bN;
        /*if(result > MAX_INT){ TODO uzdet tikrinima
            RealMachine.setCF();
        }*/

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
    }

    public void DIV(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = bN / aN;
        /*TODO uzdet tikrinima
        if(result > MAX_INT){
            RealMachine.setCF();
        }*/

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
    }

    public void PUSH(int address){

        //TODO if SP < 224 --> klaida kazkokia
        virtualCPU.decreaseSP();
        Word word = virtualMemory.read(address);
        virtualMemory.write(word, virtualCPU.getSP());
        System.out.println(virtualCPU.getSP());
    }

    public void POP(int address){
        //TODO patikrint ar neina uz atminties ribu
        Word word = virtualMemory.read(virtualCPU.getSP());
        virtualMemory.write(word, address);
        virtualMemory.writeString("0000" , virtualCPU.getSP());
        virtualCPU.increaseSP();
    }

    //~~~~~~~~~~~~~~~~COMMANDS~~~~~~~~~~~~~~~~
}
