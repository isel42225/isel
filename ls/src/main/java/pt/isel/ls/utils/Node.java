package pt.isel.ls.utils;

import pt.isel.ls.commands.Command;

import java.util.ArrayList;

public abstract class Node {
    public String template;
    public Command cmd;
    public ArrayList<Node> children;

    public Node(String template) {
        children = new ArrayList<>();
        this.template = template;
    }

    public static void print(Node root){
        Node curr = root;
        int idx = 0;
        while(idx < curr.children.size()){
            curr = curr.children.get(idx);
            print(curr);
            ++idx;
            curr = root;
        }
        if(curr.cmd != null )System.out.println(curr.cmd.getDescription());
    }

    public abstract boolean compareTemplate(String t);

    @Override
    public abstract boolean equals(Object o);

}
