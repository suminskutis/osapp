package com.company;


public class VirtualCPU {

    private int PC;
    private int SP;
    private int PID;


    public VirtualCPU(){
        setPC(0);
        setSP(256);
        setPID(PID);
    }


    public void setPC(int PC) {
        this.PC = PC;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }


    public int getPC() {
        return PC;
    }

    public int getSP() {
        return SP;
    }

    public int getPID() {
        return PID;
    }

    public void increaseSP(){
        SP++;
    }

    public void decreaseSP(){
        SP--;
    }

    public void increasePC(){
        PC++;
    }

    public void decreasePC(){
        PC--;
    }
}
