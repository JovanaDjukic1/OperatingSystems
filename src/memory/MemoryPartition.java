package memory;

import processes.Process;

import java.util.ArrayList;

public class MemoryPartition {
    private int base;
    private int limit;
    private Process proces;
    private final int SIZE;
    private int occupied;


    public MemoryPartition(int size){
        this.SIZE = size;
        occupied = 0;
    }

    public MemoryPartition( MemoryPartition p1, MemoryPartition p2){
        SIZE = p1.getSize() + p2.getSize();
        occupied = 0;
    }

    public MemoryPartition occupiedMemory(Process proces){
        freeMemory();
        if(proces.getSize() > this.SIZE)
            return null;

        this.proces = proces;
        occupied = proces.getSize();

        return this;
    }
    public MemoryPartition occupiedMemory(int velicina){
        if(velicina > this.SIZE)
            return null;

        occupied = velicina;
        return this;
    }
    public void freeMemory(){
        if(this.proces!=null){
            this.proces.setPartition(null);
            this.proces = null;
        }
    }


    public Process getProces() {
        return proces;
    }
    public int getSize() {
        return SIZE;
    }
    public int getOccupied() {
        return occupied;
    }
    public void setOccupied(int occupied){
        this.occupied = occupied;
    }
    public int getFree(){
        return SIZE - occupied;
    }
    public int getBase(){
        return base;
    }
    public int getLimit(){
        return limit;
    }
    public void setLimit(int limit){
        this.limit = limit;

    }
    public void setBase(int base){
        this.base = base;
    }

    @Override
    public String toString() {
        if(this.occupied==0){


            String res = "|";
            for(int i = 0; i<this.getSize()/10; i++) {
                if( i % 40 == 0 && i!=0)
                    res+="\n ";
                res += ("o");
            }
            res += "|";
            return res;
        }
        int k, z, s;
        k = (SIZE%10 < 5) ? SIZE/10 : SIZE/10+1;
        z = (occupied%10 < 5) ? occupied/10 : occupied/10+1;
        s = k - z;

        String res = "|";
        for(int i = 0; i<z; i++)
            res += "x";
        for(int i = 0; i<s; i++)
            res += " ";
        res += "|\n";
        return res;
    }
    public String info() {
        String nazivProcesa = (proces==null) ? "N.P." : proces.getName();
        return String.format("Process name: %s; size: %d; (occupied: %d; free: %d)", nazivProcesa, SIZE, occupied, SIZE - occupied);
    }








    //Trazimo slobodne odgovarajuce particije
    public static ArrayList<MemoryPartition> getFreePartitions(Process proces) {
        ArrayList<MemoryPartition> freePartitions = new ArrayList<>();
        for(MemoryPartition partition: Memory.getPartitions())
            if(partition.getOccupied() == 0 && partition.getSize() >= proces.getSize() && partition.getProces() == null)
                freePartitions.add(partition);
        return freePartitions;
    }

  /*  //Trazimo zauzete odgovarajuce particije
    public static ArrayList<MemoryPartition> getOdgovarajuceParticije(Process proces) {
        ArrayList<MemoryPartition> odgovarajuceParticije = new ArrayList<>();
        for(MemoryPartition particija: Memory.getParticije())
            if(particija.getSize() >= proces.getVelicina())
                odgovarajuceParticije.add(particija);
        return odgovarajuceParticije;
    }

    public static ArrayList<MemoryPartition> getZauzeteParticije() {
        ArrayList<MemoryPartition> zauzeteParticije = new ArrayList<>();
        for(MemoryPartition particija: Memory.getParticije())
            if(particija.getProces() != null)
                zauzeteParticije.add(particija);
        return zauzeteParticije;
    } */
}

