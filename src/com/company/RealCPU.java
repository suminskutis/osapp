package com.company;


public class RealCPU {

    public static final int SUPERVISOR = 0;
    public static final int USER = 1;


    private static int PTR;
    private static int PC;
    private static int SP;
    private static int SM;
    private static int PID;
    private static int MODE;
    private static int TI;
    private static int PI;

    private static int SI;
    private static int CH1;
    private static int CH2;
    private static int CH3;

    private static int supervisor = 0;
    private static int time = 10;

    public static int[] sf = {0, 0, 0};   //0-SF, 1-ZF, 2-CF


    public static void printFlags(){
        System.out.println("Status Flags: SF:" + sf[0] + " ZF:" + sf[1] + " CF:" + sf[2]);
    }


    public RealCPU() {
        setPTR(0);
        setPC(0);
        setSP(0);
        setSM(0);
        setPID(0);
        setTI(time);
        setPI(0);
        setSI(0);
        setCH1(0);
        setCH2(0);
        setCH3(0);
        setMODE(supervisor);
    }

    public static int getPTR() {
        return PTR;
    }

    public static void setPTR(int PTR) {
        RealCPU.PTR = PTR;
    }

    public static int getPC() {
        return PC;
    }

    public static void setPC(int PC) {
        RealCPU.PC = PC;
    }

    public static int getSP() {
        return SP;
    }

    public static void setSP(int SP) {
        RealCPU.SP = SP;
    }

    public void resetInterrupts() {
        resetTI();
        SI = 0;
        PI = 0;
    }

    public void resetTI() {
        setTI(time);
    }

    public int getInterrupt() {
        if (TI == 0) {
            return 1;
        }

        switch (PI) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
        }

        switch (SI) {
            case 1:
                return 5;
            case 2:
                return 6;
        }

        return 0;
    }

    public static void decreaseTI(){
        TI--;
    }

    public int getSM() {
        return SM;
    }

    public static void setSM(int SM) {
        RealCPU.SM = SM;
    }

    public int getPID() {
        return PID;
    }

    public static void setPID(int PID) {
        RealCPU.PID = PID;
    }

    public int getMODE() {
        return MODE;
    }

    public static void setMODE(int MODE) {
        RealCPU.MODE = MODE;
    }

    public int getTI() {
        return TI;
    }

    public static void setTI(int TI) {
        RealCPU.TI = TI;
    }

    public int getPI() {
        return PI;
    }

    public static void setPI(int PI) {
        RealCPU.PI = PI;
    }

    public int getSI() {
        return SI;
    }

    public static void setSI(int SI) {
        RealCPU.SI = SI;
    }

    public int getCH1() {
        return CH1;
    }

    public static void setCH1(int CH1) {
        RealCPU.CH1 = CH1;
    }

    public int getCH2() {
        return CH2;
    }

    public static void setCH2(int CH2) {
        RealCPU.CH2 = CH2;
    }

    public int getCH3() {
        return CH3;
    }

    public static void setCH3(int CH3) {
        RealCPU.CH3 = CH3;
    }


    //0-SF, 1-ZF, 2-CF

    public static void setSF(){
        sf[0] = 1;
    }
    public static void setZF(){
        sf[1] = 1;
    }
    public static void setCF(){
        sf[2] = 1;
    }


    public static int getSF(){
        return sf[0];
    }
    public static int getZF(){
        return sf[1];
    }
    public static int getCF(){
        return sf[2];
    }
}
