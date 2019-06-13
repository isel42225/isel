package pt.isel.ls.utils;

public class NodeLiteral extends Node {


    public NodeLiteral(String template) {
        super(template);
    }


    @Override
    public boolean compareTemplate(String t) {
        return template.equalsIgnoreCase(t);
    }

    @Override
    public boolean equals(Object o) {
        NodeLiteral n = null;
        if(o != null && o instanceof NodeLiteral){
            n = (NodeLiteral)o;
        }
        return n != null && this.template.equals(n.template);
    }


}
