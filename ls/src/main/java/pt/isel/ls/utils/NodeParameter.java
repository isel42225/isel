package pt.isel.ls.utils;



public class NodeParameter extends Node {


    public NodeParameter(String template) {
        super(template);
    }



    @Override
    public boolean compareTemplate(String t) {
        t = t.trim();
        if(t.charAt(0) > '0' && t.charAt(0) < '9')
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NodeParameter;
    }
}
