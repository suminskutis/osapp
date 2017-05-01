package com.company;


import java.util.Random;

public class PageController {

    private static Page[] pages = new Page[RealMachine.RM_MEMORY_BLOCKS];

    public static Page findFreePage() {
        Random rand = new Random();
        int randomNum;
        do {
            randomNum = rand.nextInt(RealMachine.RM_MEMORY_BLOCKS) ;
            if(pages[randomNum] == null)
                pages[randomNum] = new Page(randomNum);

        } while (pages[randomNum].isAllocated());
        pages[randomNum].allocate();
        return pages[randomNum];
    }

    public void deAllocatePage(int virtualPageIndex){
        if(pages[virtualPageIndex].isAllocated()){
            pages[virtualPageIndex].deAllocate();
        }
    }

    public boolean IsAllocatedPage(int virtualPageIndex) {
        return pages[virtualPageIndex].isAllocated();
    }

}
