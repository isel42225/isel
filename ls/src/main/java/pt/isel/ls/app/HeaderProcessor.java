package pt.isel.ls.app;

import pt.isel.ls.utils.Pair;

import java.util.HashMap;

public class HeaderProcessor {

    public static final String FILE_NAME = "file-name";
    private final static String DEFAULT_VIEW = "text/html";
    private String view = DEFAULT_VIEW;    //formato texto
    private String out ;  //local print do texto

    private  HashMap<String , String> headers = new HashMap<>();

    public void splitHeader(String header){
        String [] split = header.split("\\|");

        for(String s : split){
          String [] pair = s.split(":");
          Pair<String , String> p = new Pair<>(pair[0], pair[1]);
          headers.put(p.key , p.value);
        }

        String v =  headers.get("accept");
        if(v != null )view = v;
        if(headers.containsKey(FILE_NAME)) out = headers.get(FILE_NAME);
    }

    public String getView() {
        return view;
    }

    public String getOut() {
        return out;
    }
}
