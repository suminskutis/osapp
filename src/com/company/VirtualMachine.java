package com.company;

public class VirtualMachine {

    private VirtualCPU virtualCPU;
    private VirtualMemory virtualMemory;
    private int index;


    public static final int VM_MEMORY_SIZE = 256;
    public static final int MAX_INT = 65535;


    final static int DATA_START = 0;
    final static int DATA_SIZE = 112;

    static final int PAGE_TABLE_START = 96;

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
                System.out.println("NULL");
            }else{
            System.out.println(word.getValue() + " ");
            }
        }
    }

    //print registers
    public void printReigsters(){
        System.out.println("PC:" + virtualCPU.getPC() + " SP:" + virtualCPU.getSP());
    }


    //~~~~~~~~~~~~~~~~COMMANDS~~~~~~~~~~~~~~~~
    public void ADD(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = aN + bN;
        if(result > MAX_INT){
            RealCPU.setCF();
        }

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1  );
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void SUB(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = bN - aN;
        if(result < 0){
            RealCPU.setSF();
        }
        if(result == 0){
           RealCPU.setZF();
        }

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();

    }

    public void MUL(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = aN * bN;
        if(result > MAX_INT){
            RealCPU.setCF();
        }

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();

    }

    public void DIV(){
        String a = virtualMemory.read(virtualCPU.getSP()).getValue();
        int aN = Integer.parseInt(a, 16);

        String b = virtualMemory.read((virtualCPU.getSP()) + 1).getValue();
        int bN = Integer.parseInt(b, 16);

        int result = (int) Math.floor(bN / aN);
        /*TODO uzdet tikrinima
        if(result > MAX_INT){
            RealMachine.setCF();
        }*/

        String resultS = Integer.toHexString(result);
        virtualMemory.writeStringToWord(resultS, virtualCPU.getSP() + 1);
        virtualCPU.increaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void PUSH(int address){

        //TODO if SP < 224 --> klaida kazkokia
        virtualCPU.decreaseSP();
        Word word = virtualMemory.read(address);
        virtualMemory.write(word, virtualCPU.getSP());
        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void POP(int address){
        //TODO patikrint ar neina uz atminties ribu
        Word word = virtualMemory.read(virtualCPU.getSP());
        virtualMemory.write(word, address);
        virtualMemory.writeString("0000" , virtualCPU.getSP());

        virtualCPU.increaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void CMP(){
        Word upper = virtualMemory.read(virtualCPU.getSP());
        Word lower = virtualMemory.read(virtualCPU.getSP()+1);

        int upperInt = upper.getIntValue();
        int lowerInt = lower.getIntValue();

        Word write = new Word();

        if(upperInt < lowerInt){
            write.setValue("0003");
            virtualMemory.write(write, virtualCPU.getSP()-1);
        }
        if(upperInt > lowerInt){
            write.setValue("0002");
            virtualMemory.write(write, virtualCPU.getSP()-1);
        }
        if(upperInt == lowerInt){
            write.setValue("0001");
            virtualMemory.write(write, virtualCPU.getSP()-1);
        }

        virtualCPU.decreaseSP();
        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void JP(String address){
        int pcAddress = Integer.parseInt(address);
        virtualCPU.setPC(pcAddress);
        RealCPU.decreaseTI();
    }

    // jei steko virsune 1
    public void JE(String address){

        if(virtualMemory.readString(virtualCPU.getSP()).equals("0001")){
            int pcAddress = Integer.parseInt(address);
            virtualCPU.setPC(pcAddress);
            RealCPU.decreaseTI();
        }else{
            virtualCPU.increasePC();
            RealCPU.decreaseTI();
        }


    }
    //jei steko virsune 3
    public void JL(String address){
        if(virtualMemory.readString(virtualCPU.getSP()).equals("0003")){
            int pcAddress = Integer.parseInt(address);
            virtualCPU.setPC(pcAddress);
            RealCPU.decreaseTI();
        }else{
            virtualCPU.increasePC();
            RealCPU.decreaseTI();
        }
    }

    //jei steko virsune 2
    public void JG(String address){
        if(virtualMemory.readString(virtualCPU.getSP()).equals("0002")){
            int pcAddress = Integer.parseInt(address);
            virtualCPU.setPC(pcAddress);
            RealCPU.decreaseTI();
        }else{
            virtualCPU.increasePC();
            RealCPU.decreaseTI();
        }
    }

    //jeigu CF  == 1
    public void JB(String address){

        if(RealCPU.getCF() == 1){
            int pcAddress = Integer.parseInt(address);
            virtualCPU.setPC(pcAddress);
            RealCPU.decreaseTI();
        }else{
            virtualCPU.increasePC();
            RealCPU.decreaseTI();
        }
    }


    public void PRTS(){
        Word word = virtualMemory.read(virtualCPU.getSP());
        OutputDevice.printString(word);

        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    public void PRTN(){
        Word word = virtualMemory.read(virtualCPU.getSP());
        OutputDevice.printInt(word);

        virtualCPU.increasePC();
        RealCPU.decreaseTI();
    }

    //~~~~~~~~~~~~~~~~COMMANDS~~~~~~~~~~~~~~~~
}
