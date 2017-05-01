package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        RealMachine rm = new RealMachine();
       // rm.run();
        rm.fillSupervisorMemFromFlash();
        System.out.println(rm.supervisorMemory.validateProgram());

    /*    Page peidzas = PageController.findFreePage();
        Page peidzas1 = PageController.findFreePage();
        System.out.println(peidzas.getPageIndex());
        System.out.println(peidzas1.getPageIndex());*/

    }
}
