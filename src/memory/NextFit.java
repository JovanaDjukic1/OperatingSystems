package memory;

import processes.Process;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class NextFit {
    private static int prc = 0;

    public static MemoryPartition loadProces(Process proces){
        int index = -1; //odg
        ArrayList<MemoryPartition> partitions = MemoryPartition.getFreePartitions(proces);

        for(MemoryPartition mp: partitions)
            if(Memory.getPartitions().indexOf(mp) > prc) {
                index = Memory.getPartitions().indexOf(mp);
                prc = index;
                break;
            }

        if(index != -1) {

            MemoryPartition particija_procesa = Memory.occupiedPartition(index, proces);
            System.out.println("Index: "+index); //odg
            Memory.splitPartition(index);
            return particija_procesa;
        }
        //Ako je prvi proces
        if(partitions.size() > 0) {
            MemoryPartition particija_procesa = Memory.occupiedPartition(0, proces);
            Memory.splitPartition(0);

            return particija_procesa;
        }
        return null;

    }
}
