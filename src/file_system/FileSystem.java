package file_system;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileSystem {
    FileNode root;
    public FileSystem(){
        root = new FileNode("",true);
    }
    public boolean getContent(String path,String fileName){
        FileNode curr = null;
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
        }
        curr = goToCurrDir(path).children.get(fileName);
        if(curr==null){
            throw new IllegalArgumentException("File not found");
        }

        System.out.println("Content " +curr.content);


        return true;

    }


    public boolean mkdir(String path){
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            return false;
        }
        String[] tokens = path.split("/");
        FileNode curr = root;
        boolean isCreated = false;
        for(int i=1; i<tokens.length; i++){
            if(!curr.children.containsKey(tokens[i])){
                curr.children.put(tokens[i], new FileNode(tokens[i], true));
                isCreated = true;
            }
            curr = curr.children.get(tokens[i]);
        }
        return isCreated;
    }

    public List<String> ls(String path){
        List<String> list = new ArrayList<>();
        FileNode curr = goToCurrDir(path);
        list.addAll(curr.children.keySet());
        Collections.sort(list);
        System.out.println("List of directorium: "+list);
        return list;
    }

    public FileNode goToCurrDir(String path) {
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
        }
        FileNode curr = root;
        String[] tokens = path.split("/");
        for(int i=1; i<tokens.length; i++){
            if(!curr.children.containsKey(tokens[i])){
                throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
            }
            curr = curr.children.get(tokens[i]);
        }
        return curr;
    }

    public void appendToFile(String path, String fileName, String content){
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
        }
        FileNode curr = goToCurrDir(path).children.get(fileName);
        if(curr==null){
            throw new IllegalArgumentException("File not found");
        }
        System.out.println("Appended content: " +content);
        curr.content.append(content);
    }

    public void createFile(String path, String fileName, String content){
        FileNode curr = goToCurrDir(path);
        if(curr.children.containsKey(fileName)){
            appendToFile(path, fileName, content);
        }else{
            curr.children.put(fileName, new FileNode(fileName, false));
            appendToFile(path, fileName, content);
        }
    }
    public void deleteFile(String path, String fileName){
        if(path.isEmpty() == true || !path.startsWith("/") || path.equals("/"))
            System.out.println("Error");
        FileNode current = root;
        int i = 1;
        String[] splits=path.split("/");
        while(i<splits.length){
            if(!current.children.containsKey(splits[i])){
                throw new IllegalArgumentException("invalid path");
            }
            current=current.children.get(splits[i]); //get(key)
            i++;

        }
        if (!current.children.containsKey(fileName)){
            System.out.println("File not exists");}
        else {
            FileNode c = current;
            //Node c=current.children.get(fileName);
            // File c = current.children.get(fileName);
            //c.children.clear();
            c.children.remove(fileName);
            System.out.println("Deleted file");
        }


    }



}
