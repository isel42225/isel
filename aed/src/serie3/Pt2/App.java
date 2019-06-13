package serie3.Pt2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


public class App {
  private static HashMap<Long,HashSet<Long>> h = new HashMap<>();

    public static void main(String[] args) {
        int count = 0;
        try(BufferedReader bf = new BufferedReader(new FileReader(args[0]))) {

            String s;
            while((s = bf.readLine()) != null){
                String i1 = s.substring(0,s.indexOf('\t'));
                String i2  = s.substring(s.indexOf('\t')).trim();

                long l1 = Long.parseLong(i1);
                long l2 = Long.parseLong(i2);

               if(h.get(l1) == null) h.put(l1,new HashSet<>());
               if(h.get(l2) == null) h.put(l2,new HashSet<>());

               if(l2 != l1)
                       h.get(l1).add(l2);


            }

            int tcount = 0;

            HashSet<Integer> hits = new HashSet<>();

            for(Long v0 : h.keySet()) {
                for (Long v1 : h.get(v0)) {
                        for (Long v2 : h.get(v1)) {
                            Integer hit = (int)(v0 + v1 + v2 );
                            if(!hits.contains(hit))
                                if (isConnect(v2, v0)){
                                    hits.add(hit);
                                    ++tcount;
                                }

                        }
                }


            }

            System.out.println(tcount);
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isConnect (long l1, long l2 ){
        return h.get(l1).contains(l2) || h.get(l2).contains(l1);
    }
}
