package com.company;


import java.io.FileNotFoundException;
import java.io.IOException;

public class RealMachine {

    private RealCPU realCPU;
    private RealMemory realMemory;
    private VirtualMachine currentVM;
    public static final int RM_MEMORY_SIZE = 4096;

    public static int[] sf = {0, 0, 0, 0}; //0-OF, 1-SF, 2-ZF, 3-CF


    public RealMachine() {
        realCPU = new RealCPU();
        realMemory = new RealMemory(RM_MEMORY_SIZE);
    }



    public void run() throws IOException {

        currentVM = new VirtualMachine();
        fillMemory(currentVM);

        int currentVMPC = currentVM.getVirtualCPU().getPC();
        int commandRead = currentVMPC + VirtualMachine.PROGRAM_START;
        Word command;
        //do until tries to access stack (<224)
        while(commandRead < VirtualMachine.STACK_START ){
            //read command at address of PC
            command = currentVM.getVirtualMemory().read(commandRead);
            //if end of commands break
            if(command == null)
                break;

            //increase PC
            currentVM.getVirtualCPU().setPC(++currentVMPC);
            //get PC value
            currentVMPC = currentVM.getVirtualCPU().getPC();
            commandRead = currentVMPC + VirtualMachine.PROGRAM_START;

            Identify(command.getValue(), currentVM);

            System.out.println(command.getValue());
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //currentVM.printMemory();
    }

    //Used to fill VirtualMemory of specified VM
    public void fillMemory(VirtualMachine virtualMachine) throws IOException {

        InputDevice.openFile();
        String line;
        Word word;
        int counter = VirtualMachine.DATA_START;

        line = InputDevice.getInputLine().getValue();
        if (line.equals("DATA")) {
            RealCPU.setCH1(1);
            word = InputDevice.getInputLine();
            RealCPU.setCH1(0);
            line = word.getValue();
            while (!(line.equals("CODE"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                RealCPU.setCH1(1);
                word = InputDevice.getInputLine();
                RealCPU.setCH1(0);
                line = word.getValue();
            }

            counter = VirtualMachine.PROGRAM_START;
            RealCPU.setCH1(1);
            word = InputDevice.getInputLine();
            RealCPU.setCH1(0);
            line = word.getValue();

            while (!(line.equals("HALT"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                RealCPU.setCH1(1);
                word = InputDevice.getInputLine();
                RealCPU.setCH1(0);
                line = word.getValue();
            }
        }
    }


    public void Identify(String line, VirtualMachine VM) /*throws CloneNotSupportedException*/ {
        if(line.substring(0, 3).equals("ADD")){
            VM.ADD();
            //System.out.println("sudedam");
        }
        else if(line.substring(0, 3).equals("SUB")){
            VM.SUB();
           // System.out.println("atimam");
        }
        else if(line.substring(0, 3).equals("MUL")){
            VM.MUL();
            // System.out.println("sudauginam");
        }
        else if(line.substring(0, 3).equals("DIV")){
            VM.DIV();
            //System.out.println("dalinam");
        }
        else if(line.substring(0, 2).equals("PU")){
            String addressS = line.substring(3,4);
            int address = Integer.parseInt(addressS, 16);
            VM.PUSH(address);

            //System.out.println("pushinam" + address);
        }
        else if(line.substring(0, 2).equals("PO")){
            String addressS = line.substring(3,4);
            int address = Integer.parseInt(addressS, 16);
            VM.POP(address);

            //System.out.println("popinam" + address);
        }
        else if(line.substring(0, 2).equals("JP")){

            //VM.cmdJP(x, y);
        }
        else if(line.substring(0, 2).equals("JE")){
            //VM.cmdJE(x, y);
        }
        else if(line.substring(0, 4).equals("FORK")){
            //VM.cmdFORK();
        }
        else if(line.substring(0, 3).equals("ISP")){
            //VM.cmdISP();
        }
        else if(line.substring(0, 4).equals("PRTS")){
            //VM.cmdPRTS();
        }
        else if(line.substring(0, 4).equals("PRTN")) {
            //VM.cmdPRTN();
        }
    }



//0-OF, 1-SF, 2-ZF, 3-CF
    public static void setOF(){
        sf[0] = 1;
    }
    public static void setSF(){
        sf[1] = 1;
    }
    public static void setZF(){
        sf[2] = 1;
    }
    public static void setCF(){
        sf[3] = 1;
    }

    public static int getOF(){
        return sf[0];
    }
    public static int getSF(){
        return sf[1];
    }
    public static int getZF(){
        return sf[2];
    }
    public static int getCF(){
        return sf[3];
    }









        // dabar nereik situ
        private static int[] indexes = new int[15];
        static {
            for (int i = 0; i < 15; i++) {
                indexes[i] = -1;
            }
        }
    public static int getFreeIndex(int PID) {

        for (int i = 0; i < 15; i++) {
            if (indexes[i] == -1) {
                indexes[i] = PID;
                return i;
            }
        }
        return -1;


    }
}

