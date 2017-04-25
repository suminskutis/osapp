package com.company;


import java.io.FileNotFoundException;
import java.io.IOException;

public class RealMachine {

    private RealCPU realCPU;
    private RealMemory realMemory;
    private VirtualMachine currentVM;
    private SupervisorMemory supervisorMemory;
    public HDD hdd;

    public static final int RM_MEMORY_SIZE = 4096;






    public RealMachine() {
        realCPU = new RealCPU();
        realMemory = new RealMemory(RM_MEMORY_SIZE);
        supervisorMemory = new SupervisorMemory();
        hdd = new HDD();

        //hdd = new HDD();
    }



    public void run() throws IOException {

        currentVM = new VirtualMachine();

        fillSupervisorMemFromFlash();
        fillHDDfromSupervisorMem();
        fillVirtualMemFromHDD(currentVM,0);


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

           // System.out.println(command.getValue());
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        currentVM.printMemory();
    }

    public void fillSupervisorMemFromFlash() throws IOException {

        supervisorMemory.initMemory();
        InputDevice.openFile();
        String line;
        Word word;
        int counter = 0;

        word = InputDevice.getInputLine();
        line = word.getValue();

        while(!(line.equals("NULL"))){
            supervisorMemory.write(word, counter++);
            word = InputDevice.getInputLine();
            line = word.getValue();
        }
    }

    public void fillHDDfromSupervisorMem(){
        hdd.initMemory();
        int count = HDD.getProgramCount();
        String programNumber = Integer.toHexString(count);
        String start = "str" + programNumber;
        String end = "end" + programNumber;
        //System.out.println(end);
        int counter = 0;
        Word word;
        String line;
        int supervisorMemSize = supervisorMemory.getSize();

        hdd.writeString(start);
        for (int i = 0; i < supervisorMemSize; i++){
            word = supervisorMemory.read(i);
            hdd.write(word);
        }
        //hdd.writeString(end);



        HDD.incProgramCount();
    }

    public void fillVirtualMemFromHDD(VirtualMachine virtualMachine, int programIndex){

        int counter = VirtualMachine.DATA_START;
        int hddPointer = 0;


        String programStart = "str" + Integer.toHexString(programIndex);
       // String programEnd = "end" + program;
        String element;
        String line;
        Word word;

        for(int i = 0; i <HDD.HDD_SIZE; i++){
            element = hdd.read(i).getValue();
            if(element.equalsIgnoreCase(programStart)){
                hddPointer = i;
                break;
            }
        }

        line = hdd.readString(++hddPointer);
        if (line.equals("DATA")) {
            word = hdd.read(++hddPointer);
            line = word.getValue();
            while (!(line.equals("CODE"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                word = hdd.read(++hddPointer);
                line = word.getValue();
            }
            counter = VirtualMachine.PROGRAM_START;
            word = hdd.read(++hddPointer);
            line = word.getValue();

            while (!(line.equals("HALT"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                word = hdd.read(++hddPointer);
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


    public  HDD getHdd() {
        return hdd;
    }

  /*  public static int getHDDIndex(){
        return getHdd().g
    }*/

    public  Word readWordFromHDD(int address){
        Word word = getHdd().read(address);
        return word;
    }

    public  void writeWordToHDD(Word word){
        getHdd().write(word);
    }







    /*    // dabar nereik situ
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


    }*/

   /* //Used to fill VirtualMemory of specified VM from file
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

    public void fillMemoryFromHDD(VirtualMachine virtualMachine) throws IOException {
        InputDevice.openFile();
        String line;
        Word word;
        int counter = VirtualMachine.DATA_START;
        int counteris = 0;

        line = getHdd().readString(counteris++);
        System.out.println(line);
        //System.out.println(line);
//        line = InputDevice.getInputLine().getValue();
        if (line.equals("DATA")) {
            RealCPU.setCH1(1);
            word = getHdd().read(counteris++);
            RealCPU.setCH1(0);;
            line = word.getValue();
            while (!(line.equals("CODE"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                RealCPU.setCH1(1);
                word = getHdd().read(counteris++);
                RealCPU.setCH1(0);
                line = word.getValue();
            }

            counter = VirtualMachine.PROGRAM_START;
            RealCPU.setCH1(1);
            word = getHdd().read(counteris++);
            RealCPU.setCH1(0);
            line = word.getValue();

            while (!(line.equals("HALT"))) {
                currentVM.getVirtualMemory().write(word, counter++);
                RealCPU.setCH1(1);
                word = getHdd().read(counteris++);
                RealCPU.setCH1(0);
                line = word.getValue();
            }
        }
        System.out.println("la");
    }

    //fill HDD from file
    public void fillHDD() throws IOException {
        InputDevice.openFile();
        String line;
        Word word;
        //int counter = VirtualMachine.DATA_START;


        word = InputDevice.getInputLine();
        line = word.getValue();
        writeWordToHDD(word);
        if (line.equals("DATA")) {
            RealCPU.setCH1(1);
            word = InputDevice.getInputLine();
            RealCPU.setCH1(0);
            line = word.getValue();
            while (!(line.equals("CODE"))) {
                writeWordToHDD(word);
                //currentVM.getVirtualMemory().write(word, counter++);
                RealCPU.setCH1(1);
                word = InputDevice.getInputLine();
                RealCPU.setCH1(0);
                line = word.getValue();
            }
            writeWordToHDD(word);

            //counter = VirtualMachine.PROGRAM_START;
            RealCPU.setCH1(1);
            word = InputDevice.getInputLine();
            RealCPU.setCH1(0);
            line = word.getValue();
            while (!(line.equals("HALT"))) {
                //currentVM.getVirtualMemory().write(word, counter++);
                writeWordToHDD(word);
                RealCPU.setCH1(1);
                word = InputDevice.getInputLine();
                RealCPU.setCH1(0);
                line = word.getValue();
            }
            writeWordToHDD(word);
        }
    }
*/
}

