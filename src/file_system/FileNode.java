package file_system;

import java.util.HashMap;

public class FileNode {
    String fileName;
    boolean isDir;
    HashMap<String, FileNode> children;
    StringBuffer content;
    public FileNode(String fileName, boolean isDir){
        this.fileName = fileName;
        this.isDir = isDir;
        this.children = new HashMap<>();
        if(!this.isDir){
            this.content = new StringBuffer();
        }
    }

    public HashMap<String, FileNode> getChildren(){
        return children;
    }
}
