package cpu;
import processes.Process;

public class ThreadHandler extends Thread {
    Process proces;

    public ThreadHandler(Process proces) {
        this.proces = proces;
        this.start();

    }
    public void run(){

        CPU.execute(proces);
    }
}
