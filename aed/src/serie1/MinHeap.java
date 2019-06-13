package serie1;

import java.util.Comparator;

public class MinHeap {
    private WordsOnFile[] array;
    private int size;
    private int maxSize;

    private static final int ROOT = 0;

    public MinHeap(int maxSize) {
        this.maxSize = maxSize;
        this.size = 0;
        array = new WordsOnFile[this.maxSize];
    }

    private int left(int root) {
        return (root << 1) + 1;
    }

    private int right(int root) {
        return (root << 1) + 2;
    }

    private void swap(int fpos, int spos) {
        WordsOnFile tmp;
        tmp = array[fpos];
        array[fpos] = array[spos];
        array[spos] = tmp;
    }

    public void insert(WordsOnFile element) {
        if(size != maxSize) {
            boolean isNotEqual = true;
            for (int i = 0; i < size && isNotEqual; ++i){
                if (array[i].getWord().equalsIgnoreCase(element.getWord()))
                    isNotEqual = false;
            }
            if(isNotEqual) {
                array[size++] = element;
            }
        }

        if(element.getNchars() > array[ROOT].getNchars()) {
            array[ROOT] = element;
            heapify(array, size, 0, (o1, o2) -> o1.getNchars() - o2.getNchars());

        }

        if(element.getNchars() == array[ROOT].getNchars()) {
            array[ROOT] = array[ROOT].getWord().compareToIgnoreCase(element.getWord()) > 0
                    ? element : array[ROOT];
            heapify(array, size, 0, (o1, o2) -> o1.getWord().compareToIgnoreCase(o2.getWord()));

        }

    }

    private  void heapify(WordsOnFile [] a, int sizeHeap, int i, Comparator<WordsOnFile> cmp) {
        int max = i;
        int lftChild = left(i);
        int rghtChild = right(i);

            //check if left child idx is out of heap bounds
            if (lftChild >= sizeHeap) return;

            if (cmp.compare(a[lftChild],a[i]) < 0) {
                max = lftChild;
            }

            //check if right child idx is out of heap bounds
            if (rghtChild < sizeHeap && cmp.compare(a[rghtChild],a[max]) < 0) {
                max = rghtChild;
            }

            //if a[i] was not correctly ordered
            if (max != i) {
                swap(i, max);
                heapify(a, sizeHeap, max,cmp); //max is new idx of original value (a[i])
            }

        }

    public WordsOnFile[] getArray() {
        return array;
    }
}