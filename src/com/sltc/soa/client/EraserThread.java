package com.sltc.soa.client;
import static java.lang.Thread.sleep;

class EraserThread implements Runnable {
    private boolean stop;
    public EraserThread( ) {}
    public void run () {
        stop = true;
        while (stop) {
            try {
                System.out.print("\010*");
                System.out.print("\010*");
                System.out.print("\010*");
                sleep(01);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}