package serie1;

import aulas.Tpcs;

import java.io.*;
import java.util.Comparator;


public class MaisProximas {
    public static final int startArg = 3;
    private static WordsOnFile[] output_words;
    private static String output_filename;
    private static String key;

    private static int equalChars(String s, String key) {
        int count = 0;

        if (s != null && s.length() > 0) {
            while (count < key.length() && s.charAt(count) == key.charAt(count)) {
                ++count;
            }
        }
        return count;

    }

    public static void main(String[] args) throws Exception {
        output_filename = args[2];
        key = args[1];

        output_words = new WordsOnFile[Integer.parseInt(args[0])];

        for (int i = startArg, j = 0; j < output_words.length && i < args.length; ++i, ++j) {
            MinHeap wordHeap = new MinHeap(Integer.parseInt(args[0]));
            BufferedReader file = new BufferedReader(new FileReader(args[i]));
            String s;
            while ((s = file.readLine()) != null) {
                wordHeap.insert(new WordsOnFile(s, equalChars(s, key)));
            }

            fillOutputArray(wordHeap.getArray());

        }

        try (PrintWriter pr = new PrintWriter(output_filename)){
            for (WordsOnFile output_word : output_words) {
                pr.print(output_word.getWord());
                pr.println();
            }
        }catch (IOException io){
            System.out.println("Error creating file");
        }

    }

    private static void fillOutputArray(WordsOnFile[] array) {
        for (int i = 0; i < output_words.length; ++i)
            if (output_words[i] == null)
                output_words[i] = array[i];
            else {
                for (int j = 0; j < array.length; ++j) {
                    if(output_words[j].getWord().equalsIgnoreCase(array[i].getWord())) break;

                    if (array[i].getNchars() > output_words[j].getNchars()) {
                        output_words[output_words.length - 1] = array[i];
                        Tpcs.bubbleSort(output_words, j, output_words.length - 1,
                                (o1, o2) -> o1.getNchars() - o2.getNchars());

                    } else {
                        if (output_words[j].getWord().compareToIgnoreCase(array[i].getWord()) > 0) {
                            output_words[output_words.length - 1] = array[i];
                            Tpcs.bubbleSort(output_words, j, output_words.length - 1,
                                    (o1, o2) -> o1.getWord().compareToIgnoreCase(o2.getWord()));
                            break;

                        }

                    }
                }
            }
    }
}