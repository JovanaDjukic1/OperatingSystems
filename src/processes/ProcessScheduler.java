package processes;

import cpu.ThreadHandler;
import memory.Memory;

import java.util.Queue;

public class ProcessScheduler {
    public static synchronized void schedule() {
        Queue<Process> readyQueue = Memory.getReadyQueue();
        Memory.sortQueue(readyQueue);
        if(!readyQueue.isEmpty() && Memory.getRunning_proces() == null) {

            Process proces = readyQueue.remove();

            Memory.setRunning_process(proces);
            proces.setState("RUNNING");
            new ThreadHandler(proces);
        }
    }


}
