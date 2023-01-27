package kernel;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//import cpu.CPU;
import assembler.Assembler;
import file_system.FileSystem;
import memory.Memory;
import processes.Process;

public class Kernel {
    private Scanner scan;
    private final static String[] commands= {"exe", "list","print","ls","mkdir", "createFile", "append", "read", "exit"};
    private static FileSystem fs;


    //Needs to be cleaned up
    public Kernel() {
        fs = new FileSystem();
        fs.mkdir("/home");
        fs.createFile("/home","aa.txt","babbh");
        fs.createFile("/home","bb.txt","jkjsks");
        fs.mkdir("/home/aaa");
        fs.ls("/home");
        fs.appendToFile("/home", "aa.txt", "jovana");
        fs.appendToFile("/home","bb.txt", "djukic");
        fs.ls("/home");
        fs.getContent("/home","aa.txt");
        fs.getContent("/home", "bb.txt");
        System.out.println();
        //fs.catFile("/home","bb.txt");
        fs.deleteFile("/home","aa.txt");
        fs.ls("/home");
        scan=new Scanner(System.in);
        Memory.init();
        this.start2();
    }
    public void start() {
        Memory.showMemory();
        try {
            while(scan.hasNextLine()) {
                String command=scan.nextLine();
                if(isValid(command)){
                    executeCommand(command);
                    Memory.showMemory();}
                else if(isValidFs(command)){
                }
                else
                    System.out.println("Error! '"+command.split(" ")[0]+"' is not recognized as a command!");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void start2() {
        String[]list= {"exe src/pr1.txt","exe src/pr2.txt src/res.txt","exe src/pr3.txt src/res.txt",
                "exe src/pr4.txt src/res.txt","exe src/pr5.txt src/res.txt","list"};
        /*
        for(int i=0; i<list.length; i++)
            executeCommand(list[i]);

         */
        executeCommand(list[4]);
        executeCommand(list[2]);
        //executeCommand(list[0]);
        executeCommand(list[0]);
        executeCommand(list[1]);
        //executeCommand(list[1]);
        executeCommand(list[0]);
        executeCommand(list[5]);

        start();

    }

    //Creates a new Process (Or executes other commands)
    public static void executeCommand(String command) {
        String[]list=command.split(" ");
        if(list[0].equals(commands[0]) && (list.length == 3 || list.length == 2)) {
            File file=new File(list[1]);
            if(file.exists()) {
                //ArrayList<String>codeAndData = new ArrayList<>();
                ArrayList<String> codeAndData = Assembler.convert(list[1]);
                System.out.println();
                System.out.println(codeAndData);
                int index=list[1].indexOf('.');
                String name=list[1].substring(0,index)+".asm";
                if(list.length == 3)
                    new Process(codeAndData,name,list[2]);
                else
                    new Process(codeAndData,name,null);
            }else {
                System.out.println("Error! File '"+list[1]+"' does not exist!");
            }
        }
        else if(list[0].equals(commands[1]) && list.length == 1)
            Process.list();

        else if(list[0].equals(commands[2]) && list.length == 1)
            Memory.showMemory();


        else if(list[0].equals(commands[8]) && list.length == 1)

            exit();

        else
            System.out.println("Error! Invalid parameters!");
    }
    private boolean isValid(String command) {
        String[]list=command.split(" ");
        if(!list[0].equals(commands[0]) && !list[0].equals(commands[1]) && !list[0].equals(commands[2])) //ovdje sam izbrisala
            return false;
        return true;
    }

    private boolean isValidFs(String command){
        if(command == null || command.isEmpty() || command.equals("/")) {
            System.out.println("Invalid command!");
            return false;
        }
        String command1 = command.split(" ")[0];
        if(command1 .equals("mkdir") ){
            if(fs.mkdir(command.split(" ")[1])) {
                System.out.println("Created directorium!");
                return true;
            }
            else{
                System.out.println("Path error!");

                return false;
            }

        }
        else if(command1.equals("ls")){
            String command2 = command.split(" ")[1];
            System.out.println(fs.ls(command2));
            return true;
        }
        else if (command1.equals("createFile")){
            fs.createFile(command.split(" ")[1], command.split(" ")[2], command.split(" ")[3]);
            System.out.println("Created a file!");
            return true;

        }
        else if (command1.equals("append")  ){
            fs.appendToFile(command.split(" ")[1], command.split(" ")[2], command.split(" ")[3]);
            return true;
        }
        else if(command1.equals("read")){
            System.out.println(fs.getContent(command.split(" ")[1], command.split(" ")[2]));
            return true;
        }
        else if(command1.equals("exit")){
            exit();
        }
        return false;
    }
    public static void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }


    public static void main(String[]args) {

        Kernel k = new Kernel();
    }
}
