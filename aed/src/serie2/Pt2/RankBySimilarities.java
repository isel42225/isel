package serie2.Pt2;

import java.io.*;
import java.util.*;

public class RankBySimilarities {
    private static class HashFile {
        String filename;
        HashMap <String,Integer> table;
        int sum;
        float cos;

        HashFile(String filename){
            this.filename = filename;
            table = new HashMap<>();
        }

        void put (String s){
            if(s == null)return;
            Integer v = table.get(s);

            if( v != null)	//Caso exista
                table.put(s,v+1);
            else
                table.put(s,1);
        }

         void doSum() {
            for (int v : table.values()) {
                sum += v * v;
            }
        }
    }

    public static void main(String[] args) throws IOException {

        HashMap <String,Boolean> stopWords = new HashMap<>();
        Scanner scn = new Scanner(new FileReader(args[0]));
        while(scn.hasNext()){
            String s = scn.next();
            s = s.toUpperCase();
            stopWords.put(s,true);
        }

        ArrayList<HashFile> files = new ArrayList<>(args.length-1);
        
        for(int i = 1; i <= args.length-1; ++i) {
            files.add(new HashFile(args[i]));
        }

        for(HashFile file : files){
            Scanner fscn = new Scanner(new FileReader(file.filename));
            while(fscn.hasNext()){
                String s = fscn.next();
                s = s.toUpperCase();
                if(!stopWords.containsKey(s))
                    file.put(s);
            }

            file.doSum();
        }


        Scanner cmdscn = new Scanner(System.in);

        String cmd = cmdscn.next(); //command input
        String txt = cmdscn.next(); //file to process

        if(cmd.equalsIgnoreCase("ranking")) {
            HashFile query = new HashFile(txt);
            Scanner queryscn = new Scanner(new FileReader(txt));
            StringBuilder print = new StringBuilder();
            while (queryscn.hasNext()) {
                String s = queryscn.next();
                query.put(s.toUpperCase());
                print.append(s).append("    ");
            }

            query.doSum();

            for (HashFile file : files) {
                HashMap<String, Integer> hash = file.table;
                int sum = 0;

                for  ( String k: query.table.keySet() ) {
                    Integer v = hash.get(k);
                    sum += v != null ? v : 0 ;
                }
                file.cos = (float) (sum / (Math.sqrt(query.sum) * Math.sqrt(file.sum)));

            }

            //Sort pelos cos e print da tabela
            files.sort((f1, f2) -> (f1.cos <= f2.cos ? 0 : -1));
            StringBuilder printVal = new StringBuilder();
            int p ;
            for (HashFile hf: files) {
                p=0;
                printVal.append(hf.filename);

                while (print.length() / 2 > p++) {
                    printVal.append(" ");

                }

                printVal.append(hf.cos);
                printVal.append("\n");
            }
            System.out.printf("%s       %s\n",query.filename, print.toString());
            System.out.printf(printVal.toString());
            System.out.println();

        }
        else{
            throw new UnsupportedOperationException("Unknown Command");
        }
    }

}
