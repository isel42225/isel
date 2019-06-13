package serie2.Pt1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Iterables {


    public static Iterable<Pair<String,Integer>> groupingEquals(Iterable<String> words) {
        return () -> new Iterator<Pair<String, Integer>>() {

            String curr, next;
            Iterator<String> it = words.iterator();
            int count;
            boolean nextValue = true; // A true tem que se obter o proximo valor
            boolean end = false;

            {   //limites
                if (it.hasNext()) {
                    next = it.next();
                }
                else
                    end = true;
            }
            @Override
            public boolean hasNext() {
                if(!nextValue) return true;
                if( end )  return false;

                curr = next;

                count = 1;

                while ( it.hasNext()) {
                    if((next = it.next()).equals(curr))count++;
                    else{
                        nextValue = false;
                        end = false;
                        return true;
                    }
                }

                nextValue = curr == null;
                end = true;
                return !nextValue;
            }

            @Override
            public Pair<String, Integer> next() {
                if (!hasNext()) throw new NoSuchElementException("sem mais elementos");
                nextValue = true; // Assinalar para obter  o proximo
                return new Pair<>(curr, count);
            }
        };


    }


    /**
     * Retornar um iteravel com os elementos que pertençam à maior
     * subsequência ordenada de src iniciada na primeira ocorrência de k.
     * @param src Iteravel a percorrer
     * @param k Ponto de partida
     * @return
     */
    public static Iterable<Integer> getSortedSubsequence(Iterable<Integer> src, int k) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    Iterator<Integer> it = src.iterator();
                    int max = k;    //Começar maximo com valor K
                    Integer curr;  //Inteiro corrente
                    boolean inCurr = false; //Se não houve next

                    @Override
                    public boolean hasNext() {
                        if (inCurr) return true; //Manter o valor se nao houve next
                        while (it.hasNext()) {
                            curr = it.next();
                            if (curr >= max) {
                                inCurr = true;
                                max = curr; //Novo máx
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public Integer next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        inCurr = false; //Houve next
                        return curr;

                    }
                };
            }
        };
   }  
}

