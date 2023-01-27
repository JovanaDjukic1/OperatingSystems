package memory;
import processes.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Memory {
    private static int SIZE;
    private static int occupied;
    private static ArrayList<MemoryPartition> partitions;
    private static Queue<Process> readyQueue = new LinkedList<>();
    private static Process running_process = null;

    public static void init(){
        SIZE = 4096;
        occupied = 0;
        partitions = new ArrayList<MemoryPartition>();
        Memory.makePartition(Memory.getSize());

    }
    public static boolean makePartition(int sizeOfPartition){
        if(SIZE - occupied < sizeOfPartition)
            return false;
        partitions.add(new MemoryPartition(sizeOfPartition));
        return true;
    }



    public static boolean mergeFreePartitions(int i){
        if(i>0 && partitions.get(i-1).getProces() == null){
            i--;
        }
        if(i >= partitions.size()-1 || partitions.get(i+1).getProces()!=null)
            return false;
        MemoryPartition newPartition = new MemoryPartition(partitions.get(i), partitions.get(i+1));
        partitions.set(i, newPartition);
        boolean condition = true;
        while(condition){
            condition = mergeFreePartitions(i);
        }
        return true;
    }

    public static boolean mergeFreePartitions(MemoryPartition partition) {
        int indexOfPartition = partitions.indexOf(partition);
        return mergeFreePartitions(indexOfPartition);
    }

    public static boolean splitPartition(int i){
        MemoryPartition memoryPartition = partitions.get(i);
        if(memoryPartition.getSize() == memoryPartition.getOccupied())
            return false;

        MemoryPartition newPartition1 = new MemoryPartition(memoryPartition.getOccupied());
        newPartition1.occupiedMemory(memoryPartition.getProces());
        MemoryPartition newPartition2 = new MemoryPartition(memoryPartition.getFree());
        partitions.set(i, newPartition1);
        partitions.add(newPartition2);

        mergeFreePartitions(i+1);
        return true;
    }

    public static MemoryPartition zauzmiParticiju(int index, int size) {
        return partitions.get(index).occupiedMemory(size);
    }
    public static MemoryPartition occupiedPartition(int index, Process proces) {
        return partitions.get(index).occupiedMemory(proces);
    }

    public static synchronized boolean load(Process proces){

        MemoryPartition mp = NextFit.loadProces(proces);
        if(mp == null)
            return false;

        proces.setPartition(mp);
        readyQueue.add(proces);
        proces.setState("READY");
        Memory.occupied = Memory.occupied + mp.getOccupied();
       // System.out.println("MP limit: " + mp.getLimit());
        return true;
    }

    public static void remove(Process proces) {
        Memory.occupied = Memory.occupied - proces.getPartition().getOccupied();
        for(int i = 0; i<Memory.getPartitions().size(); i++){
            if (Memory.getPartitions().get(i) == proces.getPartition())
                Memory.getPartitions().get(i).setOccupied(0);
        }
        proces.getPartition().freeMemory();
    }


    public static String info() {
        String s = "";
        for(MemoryPartition particija:partitions)
            s += particija.info() + "| ";
        return s;
    }

    public static String showMemory() {
        System.out.println("Size of memory " + SIZE);
        System.out.println("Occupied: " + occupied);
        System.out.println("Free:" + (SIZE-occupied));
        for(MemoryPartition mempart: Memory.getPartitions()){
            System.out.println("Size of partition: " + mempart.getSize());
        }
        String res = "";
        int d = 0;
        for(MemoryPartition particija:partitions) {
            res += particija;
            d += particija.toString().length();
            if(d>90) {
                d = 0;
                res += "\n";
            }
        }
        return res;
    }
    public static String decToBinary(int n){
        String binaryNumber="";
        int[] binaryNum = new int[1000];
        int i = 0;

        while (n > 0) {
            binaryNum[i] = n % 2;
            n = n / 2;
            i++;
        }
        for (int j = i - 1; j >= 0; j--)
            binaryNumber+=String.valueOf(binaryNum[j]);

        return binaryNumber;
    }


    public static int getSize() {
        return SIZE;
    }
    public static ArrayList<MemoryPartition> getPartitions(){
        return partitions;
    }
    public static Process getRunning_proces(){
        return running_process;
    }
    public static void removeRunningProcess() {
        running_process=null;
    }
    public static void setRunning_process(Process proces){
        running_process = proces;
    }
    public static Queue<Process> getReadyQueue(){
        return readyQueue;
    }

    public static int powerOfTwo(int size) {
        int i=1;
        int counter=0;
        while(i<=size) {
            i*=2;
            counter++;
        }
        if (i/2 == size)
            return --counter;
        return -1;
    }



    static void FrontToLast(Queue<Process> q,int qsize){
        if(qsize <= 0) return;
        q.add(q.peek());
        q.remove();
        FrontToLast(q,qsize-1);
    }
    static void pushInQueue(Queue<Process>q,Process proces,int qsize){
        if(q.isEmpty() || qsize == 0){
            q.add(proces);
            return;
        }
        else if (proces.getSize() <= q.peek().getSize()){
            q.add(proces);
            FrontToLast(q,qsize);
        }
        else{
            q.add(q.peek());
            q.remove();
            pushInQueue(q,proces,qsize-1);

        }

    }
    public static Queue<Process> sortQueue(Queue<Process> q){
        if(q.isEmpty()) return null;
        Process temp = q.peek();
        q.remove();
        sortQueue(q);
        pushInQueue(q,temp,q.size());
       return q;
    }

}
