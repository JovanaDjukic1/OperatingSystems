package processes;

import cpu.CPU;
import kernel.Kernel;
import memory.Memory;
import memory.MemoryPartition;

import java.util.ArrayList;
import java.util.Queue;


public class Process {

    private static ArrayList<Process> processes = new ArrayList<>();
    private MemoryPartition partition;
    private int indexParticije;
    private static int counter = 0;
    private int pid;
    private String state;
    private int programCounter;
    private int size;
    private int base;
    private int limit;
    private String name;
    public ArrayList<String> codeAndData;  //private

    private String file;
    public String currentPC;
    public String currentR1;
    public String currentR2;
    public String currentR3;
    public int currentId;

    public Process(ArrayList<String> codeAndData, String name, String file) {
        pid = counter++;
        state = "NEW";
        programCounter = 0;
        this.codeAndData = codeAndData;
        this.name = name;
        this.file = file;
        size = codeAndData.size()*16;
        int length=Memory.powerOfTwo(Memory.getSize());
        System.out.println("Length:"+length);
        String firstInstruction="";
        for(int i=0; i<length; i++) {
            firstInstruction+="0";
        }

        currentPC = firstInstruction;
        currentR1 = "";
        currentR2 = "";
        currentR3 = "";
        currentId = 0;
        processes.add(this);
        this.partition = null;
        this.init();
    }

    public void init() {
        load();
    }

    public void load() {
        boolean loaded=false;
        while(!loaded) {
            loaded = Memory.load(this);
        }

        if(Memory.getRunning_proces() == null)
            ProcessScheduler.schedule();
    }
    public void exit() {
        this.state = "TERMINATED";

        Memory.removeRunningProcess();
        Memory.remove(this);
        //TODO
        ProcessScheduler.schedule();
    }

    public void contextSwitch() {
        this.state="READY";
        this.currentPC = CPU.PC.getValue();
        this.currentR1 = CPU.R1.getValue();
        this.currentR2 = CPU.R2.getValue();
        this.currentR3 = CPU.R3.getValue();
        this.currentId = CPU.d;
        CPU.setToZero();
        Memory.removeRunningProcess();
        Memory.getReadyQueue().add(this);
        ProcessScheduler.schedule();
        Kernel.executeCommand("list");
        System.out.println("===============CONTEXT SWITCH===========");
    }

    public static void list() {
        Queue<Process> readyProcesses = Memory.getReadyQueue();

        Process runningProcess = Memory.getRunning_proces();
        if(runningProcess == null && readyProcesses.isEmpty())
            System.out.println("There are no processes that are currently in ready or running state.");
        else {
            System.out.println("List of processes:");
            if(runningProcess != null) {
                System.out.println("\tPID: "+runningProcess.pid);
                System.out.println("\tName: "+runningProcess.name);
                System.out.println("\tState: "+runningProcess.state);
                System.out.println("\tSize: "+runningProcess.size);
            }
            if(!readyProcesses.isEmpty()) {
                for(Process proces : readyProcesses) {
                    System.out.println("\tPID: "+ proces.pid);
                    System.out.println("\tName: "+ proces.name);
                    System.out.println("\tState: "+ proces.state);
                    System.out.println("\tSize: "+ proces.size);
                }
            }
            System.out.println();
        }
    }
    public int getPid() {
        return pid;
    }
    public String getFile() {
        return file;
    }
    public void setState(String state) {
        this.state=state;
    }

    public int getSize(){
        return size;
    }
    public String getName(){
        return name;
    }
    public MemoryPartition getPartition(){
        return partition;
    }
    public void setPartition(MemoryPartition p){
        this.partition = p;
    }

}

