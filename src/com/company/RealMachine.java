package com.company;


import java.io.IOException;
import java.util.Scanner;

public class RealMachine {

    private RealCPU realCPU;
    private RealMemory realMemory;
    private VirtualMachine currentVM;
    public SupervisorMemory supervisorMemory;
    public HDD hdd;

    public static final int RM_MEMORY_BLOCKS = 256;



    public RealMachine() {
        realCPU = new RealCPU();
        realMemory = new RealMemory();
        supervisorMemory = new SupervisorMemory();
        hdd = new HDD();
    }

    public static VirtualMachine createVM(){
        RealCPU.setMODE(RealCPU.SUPERVISOR);
        Page pageTable = PageController.findFreePage();
        int pageTableRealAddress = pageTable.getPageIndex();

        VirtualMachine VM = new VirtualMachine();

        for(int i = 0; i < 16; i++){
            Page page = PageController.findFreePage();
            VM.getVirtualMemory().write(Word.intToWord(page.getPageIndex()), VirtualMachine.PAGE_TABLE_START + i);
        }
        return VM;
    }



    public void run() throws IOException {

        fillSupervisorMemFromFlash();
        //supervisorMemory.validateProgram();

        //bad program format
        if(!supervisorMemory.validateProgram()){
            realCPU.setPI(3);
        }

        fillHDDfromSupervisorMem();
        currentVM = createVM();

        fillVirtualMemFromHDD(currentVM,0);


        realCPU.setMODE(1);
        System.out.println("Enter 1 for step mode, 2 to execute whole program. You can execute the rest of your program even after few steps. Press 3 at any time to quit.");
        Scanner sc = new Scanner(System.in);
        String input = "";

        int currentVMPC = currentVM.getVirtualCPU().getPC();
        int commandIndex = currentVMPC + VirtualMachine.PROGRAM_START;
        Word command;

        while(!(input = sc.next()).equals("3")){
            switch (input) {
                case "1" :
                    System.out.println("%% step %%");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    //gaunam pc
                     currentVMPC = currentVM.getVirtualCPU().getPC();
                     commandIndex = currentVMPC + VirtualMachine.PROGRAM_START;
                     //patikrinam ar nezengia i steka
                     if(commandIndex == VirtualMachine.STACK_START){
                         //System.out.println("pasiekem steka. quit");
                         input = "3";
                         break;
                     }

                     //nusiskaitom komanda
                    command = currentVM.getVirtualMemory().read(commandIndex);
                     //patikrinam ar komanda ne Halt
                    if(command.getValue().equals("HALT")){

                        input = "3";
                        break;
                    }
                    //ivykdom komanda
                    Identify(command.getValue(), currentVM);

                    currentVM.printReigsters();
                    RealCPU.printFlags();
                    currentVM.getVirtualMemory().printBlock(0);
                    break;

                case "2":
                    System.out.println("%% Executing program %%");


                     currentVMPC = currentVM.getVirtualCPU().getPC();
                     commandIndex = currentVMPC + VirtualMachine.PROGRAM_START;
                    //Word command;
                    String com;
                    //do until tries to access stack (<224)
                    while(commandIndex < VirtualMachine.STACK_START ){
                        //read command at address of PC
                        command = currentVM.getVirtualMemory().read(commandIndex);
                        //break if HALT
                        if((com = command.getValue()).equals("HALT")){
                            //System.out.println("BREIKINOM");
                            break;
                        }
                        //execute command
                        Identify(command.getValue(), currentVM);
                        //get increased cpu PC value
                        commandIndex = currentVM.getVirtualCPU().getPC() + VirtualMachine.PROGRAM_START;

                    }


                    currentVM.getVirtualMemory().printBlock(15);
                    currentVM.printReigsters();
                    //RealCPU.printFlags();

                    input = "3";
                    break;
            }
            if(input.equals("3")){
                System.out.println("%% Exiting %%");
                break;
            }
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //System.out.println(realCPU.getTI());
        
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
            currentVM.getVirtualMemory().write(word, counter++);
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
        else if(line.substring(0, 3).equals("CMP")){
                VM.CMP();
        }
        else if(line.substring(0, 2).equals("JP")){
            String address = line.substring(3,4);
            VM.JP(address);
        }
        else if(line.substring(0, 2).equals("JE")){
            String address = line.substring(3,4);
            VM.JE(address);
        }
        else if((line.substring(0, 2).equals("JL"))){
            String address = line.substring(3,4);
            VM.JL(address);
        }
        else if((line.substring(0, 2).equals("JG"))){
            String address = line.substring(3,4);
            VM.JG(address);

        }
        else if((line.substring(0, 2).equals("JB"))) {
            String address = line.substring(3,4);
            VM.JB(address);

        }
        else if(line.substring(0, 4).equals("PRTS")){
            VM.PRTS();
        }
        else if(line.substring(0, 4).equals("PRTN")) {
            VM.PRTN();
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

