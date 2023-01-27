package assembler;

import cpu.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Assembler {

    private static String jumpOpCode="0001";
    private static String loadOpCode="0010";
    private static String storeOpCode="0011";
    private static String addOpCode="0100";
    private static String subOpCode="0101";
    private static String mulOpCode="0110";
    private static String divOpCode="0111";
    private static String jnzOpCode="1000";
    private static String cmpOpCode="1001";

    private static Register R1=new Register("R1","10");
    private static Register R2=new Register("R2","11");
    private static Register R3=new Register("R3","01");

    public static ArrayList<String> convert(String filename) {

        ArrayList<String>codeList=new ArrayList<String>();
        ArrayList<Integer>indexes=new ArrayList<Integer>();

        HashMap<String,String>nameMap=new HashMap<>();

        int addressCounter=0;
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                if(line.isEmpty())
                    continue;

                String[] array = line.split(" ");

                if(array.length == 1) {

                    if(array[0].equals("HLT")) {
                        codeList.add("0000000000000000");
                        addressCounter++;
                    }
                }
                else if(array.length == 3) {

                    ArrayList<String>list=new ArrayList<>();

                    if(array[0].equals("ADD") || array[0].equals("SUB") || array[0].equals("MUL") || array[0].equals("DIV")) {
                        list=operations(array);

                        if(isNumeric(array[2])) {
                            nameMap.put(array[2], "-1");

                            //System.out.println(nameMap);


                        }
                    }
                    if(array[0].equals("MOV")) {

                        String tmp=loadOrStore(array);
                        codeList.add(tmp);
                        indexes.add(codeList.indexOf(tmp));
                        if(isNumeric(tmp.substring(6))) {
                            nameMap.put(tmp.substring(6), "-1");
                        }
                        addressCounter++;

                    }else {
                        int size=list.size();
                        addressCounter+=size;

                        for(int i=0; i<list.size(); i++) {
                            codeList.add(list.get(i));

                            boolean added=false;
                            for(int j=0; j<list.get(i).length(); j++) {
                                if(list.get(i).charAt(j) >'1' || list.get(i).charAt(j) <'0') {
                                    indexes.add(codeList.indexOf(list.get(i)));
                                    added=true;
                                    break;
                                }
                            }
                            if(added)
                                continue;
                            if(list.get(i).length() != 16) {
                                indexes.add(codeList.indexOf(list.get(i)));
                            }
                        }
                    }
                }

            }
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Print*********************codeList**********");
        //System.out.println(codeList);
        codeList.add("0000000000000000");
        //System.out.println(codeList);
        addressCounter++;

        for(int i=0; i<indexes.size(); i++) {
            int indeks = indexes.get(i);
            String pr = codeList.get(indeks).substring(6);
            String binaryNumber=decToBinary(Integer.parseInt(pr));

            String number="";

            for(int j=0; j<16-binaryNumber.length(); j++)
                number+="0";
            number+=binaryNumber;
            codeList.add(number);

            String num=decToBinary(addressCounter*16);
            String newNumber="";
            for(int j=0; j<10-num.length(); j++)
                newNumber+="0";
            newNumber+=num;
            nameMap.replace(pr,newNumber);
            addressCounter++;
            String address=nameMap.get(pr);
            String newInstr=codeList.get(indeks).substring(0,6);
            for(int j=0; j<10-address.length(); j++)    //dodamo do 10 jer 6 je vec potroseno na opCode i registar
                newInstr+="0";
            newInstr+=address;
            codeList.set(indexes.get(i), newInstr);
        }

        //System.out.println("****************");
        //System.out.println(nameMap);
        //System.out.println(indexes);
        //System.out.println(codeList);

        return codeList;
    }

    public static ArrayList<String> operations(String[] array) {

        ArrayList<String>codeList=new ArrayList<String>();
        String tmp="";

        if(isNumeric(array[2])) {

            String reg=array[1].substring(0, array[1].length()-1);
            String broj="";
            broj=array[2];

            tmp=loadOpCode+""+R3.getAddress()+""+broj;
            codeList.add(tmp);
            tmp="";

            if(reg.equals("R1")) {

                if(array[0].equals("ADD"))
                    tmp=addOpCode+"0000"+R1.getAddress()+"0000"+R3.getAddress();
                else if(array[0].equals("SUB"))
                    tmp=subOpCode+"0000"+R1.getAddress()+"0000"+R3.getAddress();
                else if(array[0].equals("MUL"))
                    tmp=mulOpCode+"0000"+R1.getAddress()+"0000"+R3.getAddress();
                else
                    tmp=divOpCode+"0000"+R1.getAddress()+"0000"+R3.getAddress();
            }else {
                if(array[0].equals("ADD"))
                    tmp=addOpCode+"0000"+R2.getAddress()+"0000"+R3.getAddress();
                else if(array[0].equals("SUB"))
                    tmp=subOpCode+"0000"+R2.getAddress()+"0000"+R3.getAddress();
                else if(array[0].equals("MUL"))
                    tmp=mulOpCode+"0000"+R2.getAddress()+"0000"+R3.getAddress();
                else
                    tmp=divOpCode+"0000"+R2.getAddress()+"0000"+R3.getAddress();
            }
            codeList.add(tmp);

        }
        else {
            String reg2=array[2];
            if(array[0].equals("ADD"))
                tmp=addOpCode+"0000"+R1.getAddress()+"0000"+R2.getAddress();
            else if(array[0].equals("SUB"))
                tmp=subOpCode+"0000"+R1.getAddress()+"0000"+R2.getAddress();
            else if(array[0].equals("MUL"))
                tmp=mulOpCode+"0000"+R1.getAddress()+"0000"+R2.getAddress();
            else
                tmp=divOpCode+"0000"+R1.getAddress()+"0000"+R2.getAddress();
            codeList.add(tmp);
        }
        return codeList;
    }
    public static String loadOrStore(String[] array) {
        String string=array[2];
        String tmp="";

        if(isNumeric(string)) {
            String reg=array[1].substring(0,array[1].length()-1); //skine se , poslije naziva registra
            if(reg.equals("R1"))
                tmp=loadOpCode+""+R1.getAddress()+""+array[2];   //   0010|10|2
            else
                tmp=loadOpCode+""+R2.getAddress()+""+array[2];
        }
        else{
            String reg=array[2];
            if(reg.equals("R1"))
                tmp=storeOpCode+""+array[1].substring(0, array[1].length()-1)+""+R1.getAddress();
            else
                tmp=storeOpCode+""+array[1].substring(0, array[1].length()-1)+""+R2.getAddress();
        }
        return tmp;
    }

    public static boolean isNumeric(String string) {

        try {
            int intValue = Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e) {

        }
        return false;
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

}
