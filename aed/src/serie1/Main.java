package serie1;

public class Main {

    public static void main(String[] args) {

    }

    //Insertion sort methods
    public static void insertSort( int[] array, int l, int r, int value ) {
        for( int i= r; i >= l; --i )
            if( array[i] > value )
                array[i+1] = array[i];
            else { // <=
                array[i+1] = value;
                return;
            }
        array[l] = value;
    }
    public static void insertionSort( int[] array, int l, int r ) {// O(n^2)e Omega(n)
        for (int i = l+1; i <=r; ++i ) {
            insertSort(array, l, i - 1, array[i]);  // O(n). Omega(1)
        }
    }

    /**
     * 21/09/2017
     * Find a value in a sorted array using binary search
     * @param a array to process
     * @param l left index (start)
     * @param r right index (end)
     * @param v value to find
     * @return index of value
     */
    public static int binarySearch(int[]a,int l, int r, int v) {
        int mid;
        while (l <= r) {
            mid = (l + r) >>> 1;

            if (a[mid] == v)
                return mid;
            if (v > a[mid])
                l = mid + 1;
            else
                r = mid - 1;
        }
        return -1;
    }

    /**
     * Copy an Array
     * @param v array to copy
     * @param l start idx
     * @param r end idx
     * @return copied array
     */
    private static int[] copyArray(int[] v, int l, int r) {
        int  []res = new int[r-l+1];
        for(int i = 0; i<res.length;++i){
            res[i] = v[l++];

        }

        return res;
    }

    /**
     * Auxiliar method swap 2 array positions
     * @param a array to process
     * @param i1 index to swap
     * @param i2 index to swap
     */
    public static void swap(int[]a, int i1, int i2) {
        int aux = a[i1];
        a[i1] = a[i2];
        a[i2] = aux;
    }

    /**
     * Indice da primeira palavra maior (lexicograficamente)
     * Recorrencia -> C(1) = O(1); C(n) = C(n/2) + O(1) -> resolvendo a recorrencia O(lg n)
     * Custo máximo O(lg n)
     * Custo minimo Omega(lg n)
     * Custo Theta(lg n)
     * @param sortedArray array ordenado
     * @param l           indice a partir do qual procura (inclusivo)
     * @param r           indice até onde procura (inclusivo)
     * @param key         palavra a procurar no array
     * @return indice da primeira palavra maior do que key
     */
    public static int upperBound(String[] sortedArray, int l, int r, String key) {
        int m;
        while ( l <= r ) {
            m=(l + r) >>> 1;
            if (key.compareToIgnoreCase(sortedArray[m]) < 0)
                r = m - 1;
            else
                l = m + 1;
        }
        return l;
    }

    public static int parent(int child){
        return (child - 1) >> 1;
    }

    public static void increaseHeapOnIndex(int [] heap, int sizeHeap, int i){
        if(i >= sizeHeap) return;

        int aux;
        while (i>1 && heap[parent(i)] < heap[i]){
            aux = (i -1) / 2;
            swap(heap, aux, i);
            i = aux;
        }
    }


                    /* ------Fim dos algoritmos auxiliares ----------- */

    public static int printEachThreeElementsThatSumTo1(int[] v, int l, int r, int s){
        int count = 0;

        for(int i = l; i<=r;++i) {
            for (int j = i + 1; j <= r; ++j) {
                for (int k = j + 1; k <= r; ++k) {
                    if (v[i] + v[j] + v[k] == s) {
                        System.out.printf("%d %d %d\n",v[i],v[j],v[k]);
                        ++count;
                    }
                }
            }
        }
        return count;
    }

    public static int printEachThreeElementsThatSumTo2(int[] v, int l, int r, int s){
        int count = 0;

        int [] cpy = copyArray(v,l,r); //to not change original
        insertionSort(cpy,0,cpy.length-1); //sort to easier search

        for(int i = 0; i< cpy.length;++i) {
            for (int j = i + 1; j < cpy.length;++j) {
                int k = s-(cpy[i]+cpy[j]);
                int idx = binarySearch(cpy,j+1,cpy.length-1,k);//O(nlg(n))
                if( (idx > -1)  && cpy[i] + cpy[j] + cpy[idx] == s  ) {
                  System.out.printf("%d %d %d\n", cpy[i], cpy[j],cpy[idx]);
                  ++count;
              }
            }
        }
        return count;
    }

    public static int printEachThreeElementsThatSumTo(int[] v, int l, int r, int s){

        int count = 0;
        int j;

        int [] aux = new int [r-l+1];
        System.arraycopy(v, l, aux,  0, aux.length);
        insertionSort(aux,0,aux.length-1);


        for (int i = 0; i <= aux.length-1; ++i){
            j= i+1;
            int k = aux.length-1;
            while(k > j) {

                if(aux[i] + aux[j] + aux[k] == s){
                    System.out.format("[%d, %d, %d]\n", aux[i], aux[j], aux[k]);
                    ++count;
                    --k;
                }
                else if (aux[i] + aux[j] + aux[k] > s) --k;

                else ++j;


            }
        }

        return count;
    }


    public static int removeIndexes(int v[], int l, int r, int[] vi, int li, int ri) {

        if(ri < 0) return v.length;
        int count = 0;

        int aux = 0;
        for (int i = l, j = li; i <= r ; ++i ) {

            if (j <= ri && aux == vi[j]) {
                if(vi[j] >= l && vi[j] <= r)
                    count++;
                j++;
                aux++;
            }
            else{
                v[i  - count] = v[i];
                aux++;
            }
        }
        return ((r-l)+1) - count;
    }

    /**
     * 3)
     * @param v array to process
     * @param l start idx
     * @param r end idx
     * @param word reference
     * @return most alike word
     */
    public static String greaterCommonPrefix(String[] v, int l, int r, String word){
       if(v.length<=0)return null; //no caso de o array não ter palavras

        String res = "";
        int count = 0;

        int upperIdx = upperBound(v,l,r,word); //indice da primeira palavra maior

        int startWordIdx = upperIdx >= v.length ? upperIdx -1: upperIdx; //escolha da 1ª palavra a comparar, testar se está dentro do array
        int startIdx = v.length - startWordIdx; //quantidade de vezes a fazer
        boolean b ;

            for(int i = startIdx; i >= l; --i,--startWordIdx) {
                b = true;
                    for (int j = 0; startWordIdx >=0 && j < v[startWordIdx].length() && j < word.length() && b; ++j) { //j -> idx do caracter da palavra
                        if (v[startWordIdx].charAt(j) != word.charAt(j)){
                            b = false;
                            count = j-1; //max caracteres em comum encontrados
                        }
                        else{
                            res = (j >=  count) ? v[startWordIdx] : res; //>= para em caso de empate escolher a última palavra
                        }
                    }
            }

        return (res.length()> 0 ? res : v[r]);
    }

    /**
     * 4)
     * @param n value of sum
     * @return number of consecutives sequences
     */
    public static int sumGivenN(int n){
        int count = 1;
        int sum = 0;
        StringBuilder s = new StringBuilder("[");
        StringBuilder res = new StringBuilder();

        for(int i = 1, j = 0; j <= n/2 && n > 0 ;){ //i = número natural ; j = indice do número natural
            if(sum > n){
                ++j;
                s = new StringBuilder("["+s.substring((""+j).length()+2)); //j.toString para tirar o número de casas decimais, +2 para cobrir o ' '.
                sum -=j;
            }
            else{
                s.append(i).append(" ");
                sum+=i++;
            }
            if( sum == n){
                res.append(s.substring(0, s.length() - 1)).append("]").append('\n'); //acrescentar à string de combinações possiveis
                ++count;
            }
        }

        System.out.printf("%s[%d]\n", res.toString(),n);

        return count;
    }

    public static int deleteMin(int[] maxHeap, int sizeHeap) {
        if(sizeHeap <=1) return 0;


        int min = maxHeap.length / 2;
        for(int i = min+1; i <= maxHeap.length-1; ++i ) {
            min = maxHeap[min] < maxHeap[i] ? min : i;
        }

       if(maxHeap[min] == maxHeap[maxHeap.length-1]) return sizeHeap-1;
        maxHeap[min] = maxHeap[sizeHeap-1];

        increaseHeapOnIndex(maxHeap,sizeHeap,min);
        return sizeHeap-1;
    }


}
