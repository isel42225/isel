package serie2.Pt2;

import java.util.*;

public class HashMap<K,V> extends AbstractMap<K, V> {
    private static final int DEFAULT_CAPACITY = 11;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private static class Node<K,V> extends AbstractMap.SimpleEntry<K,V> {
        private final int hc;
        private Node<K,V> next;


        private Node(K key, V value, int hc, Node<K,V> n) {
            super( key, value );
            this.hc = hc;
            next = n;
        }
     }

    // << Variaveis de instancia >>
    private final float lf;
    private Node<K,V> [] table;
    private int size = 0, limit;


    //<< Construtores >>
    public HashMap( int initialCapacity, float lf ) {
        this.lf = lf;
        table = new Node[initialCapacity];
        limit = (int)(table.length * lf);
    }
    public HashMap( ) {
        this( DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR );
    }
    public HashMap(Map<? extends K, ? extends V> m) {
        this(m.size(), DEFAULT_LOAD_FACTOR);
        putAll(m);
    }

    /* Métodos que têm que ser obrigatoriamente redefinidos
     * num Map modificável.
     * entrySet e put
     */

    /**
     * Obter uma visão dos mapeamentos contidos neste mapa.
     * Qualquer alteração da visão é refletida neste mapa e vice-versa.
     * @return o conjunto dos mapeamentos contidos neste mapa.
     */
    @Override
    public Set<Entry<K,V>> entrySet() { return entrySet; }
    private Set<Entry<K,V>> entrySet = new  AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {

                return new Iterator<Entry<K, V>>() {
                    int i = 0;
                    Node curr = table[i];
                    @Override
                    public boolean hasNext() {
                        while (i < table.length && curr == null){
                            curr = table[i++];
                        }
                        return curr != null;
                    }

                    @Override
                    public Entry<K, V> next() {
                        if(!hasNext()) throw new NoSuchElementException("No Elements!");
                        Node<K,V> res = curr;
                        curr = curr.next;
                        return res;
                    }
                };

            }
            @Override
            public int size() {
                return size;
            }

            /* M?todos que t?m que ser redefinidos OBRIGAT?RIAMENTE no SET
             * embora tenham herdado uma implementa??o de AbstractSet
             */

           /**
            * Verificar se existe uma entrada com a chave e valor especificado
            * na entrada o.
            * @param o entrada
            * @return true caso exista
            */
            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Entry))
                    return false;
                Entry<K, V> e = (Entry) o;

                Node n = getNode(table[index(o.hashCode())], e.getKey(), o.hashCode());

                return n != null && o.equals(n);
            }
            @Override
            public void clear() {
                HashMap.this.clear();
            }

           /**
            * Remove a entrada para a chave especificada somente se
            * estiver atualmente mapeada para o valor especificado na entrada
            * o.
            * @param o entrada
            * @return true caso a entrada tenha sido removida.
            */
            @Override
            public boolean remove(Object o) {
                if ( !( o instanceof Entry ) )
                    throw new IllegalArgumentException(" Object not an Entry! ");

                if(contains(o)){
                    HashMap.this.remove(o);
                    return true;
                }
                return false;
            }
    };

    /**
     * Calculate idx of object based on his hashcode
     * @param hc object hashcode
     * @return
     */
    private int index(int hc){
        return (hc & 0x7fffffff ) % table.length;
    }


    /**
     * Associa o valor v à chave k no mapa.
     * Se o mapa já contiver um mapeamento para a chave, substitui-o
     * pelo novo valor.
     * @param k chave
     * @param v valor a associar
     * @return o valor que se encontrava associado à chave, ou null caso
     *         não exista.
     */
    @Override
    public V put( K k, V v  ) { // Implementar O(1)
        if ( k == null )
            throw new IllegalArgumentException("not support null keys");

        int hc = k.hashCode();
        int idx = index(hc);
        Node<K, V > curr = getNode(table[idx], k, hc );
        if(size >= limit)
            expand();
        if(curr == null) {
            table[idx] = new Node<>(k, v, hc,table[idx]);
            ++size;
            return null;
        }
        else {
            return curr.setValue( v );
        }



    }

    /**
     * Expandir a tabela colocando as entradas na no indice correcto
     */
    private void expand() {
        Node<K,V>[] aux = Arrays.copyOf(table, table.length * 2);
        clear();
        table = new Node[aux.length];
        limit = (int) (lf * table.length);
        for(Node<K,V> n : aux)
            if(n != null) put(n.getKey(),n.getValue());

    }

    /**
     * Ver se esse Node existe na table
     * @param head cabeça da lista no idx definido pelo hashcode
     * @param o key a comparar
     * @param hc hashcode a comparar
     * @return Null se nao existir e Node onde já existe
     */
    private Node<K,V> getNode(Node<K,V> head, Object o, int hc ) {
        while (head != null) {
            if (hc == head.hc && o.equals(head.getKey())) return head;
            head = head.next;
        }
        return null;
    }


    /* Métodos que têm ser redefinidos OBRIGATORIAMENTE no Map
     * embora tenham herdado uma implementa??o de AbstractMap
     */

    /**
     * Obter o valor associado à chave k.
     * @param k chave
     * @return o valor ao qual a chave key está mapeada, ou null, se o mapa
     *         n?o contiver um mapeamento para essa chave.
     */
    @Override
    public V get( Object k ) {
        int idx = index(k.hashCode());
        return containsKey(k) ? table[idx].getValue() : null;
    }

    /**
     * Verificar se existe um mapeamento para a chave key.
     * @param key chave
     * @return true se este mapa contiver um mapeamento para a chave k
     */
    @Override
    public boolean containsKey(Object key) {
        int idx = index(key.hashCode());
        if(idx >= table.length) return false;
        return getNode(table[idx],key,key.hashCode()) != null;
    }

    /**
     * Remove o mapeamento de uma chave deste mapa se estiver presente.
     * @param k chave
     * @return o valor ao qual este mapa associou anteriormente a chave
     *         ou null se o mapa n?o continha mapeamento para a chave.
     */
    @Override
    public V remove( Object k ) {
        int idx = index(k.hashCode());

        Node<K,V> curr = getNode(table[idx], k, k.hashCode() ) ;
        if(curr== null) return null;

        V v = curr.getValue();
        size = v == null ? size : --size;

        if(curr.next == null)  table[idx] = null;
        else curr.next = curr.next.next;
        return v;
    }

    @Override
    public void clear() {
        Arrays.fill( table , null );
        size = 0;
    }


}
