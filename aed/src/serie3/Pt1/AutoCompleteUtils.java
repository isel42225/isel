package serie3.Pt1;


import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class AutoCompleteUtils {
   public final  static int DIM = 'z'-'a' + 1;


    public static class TNode{
            boolean isWord;
            TNode children[] = new TNode[DIM];
      }


    public static void main(String[] args)  {
        TNode f1 =  loadWordsFromFile(new TNode(), "f1.txt" );
        TNode pref =  longestWithPrefix(f1,"aaab");
        System.out.println(countPossibleWords(f1,"wsrilk"));
    }

    /**
     * Armazena, numa árvore n-ária referenciada por root,
     * todas as palavras que ocorrem no ficheiro de texto.
     * @param root Inicio da arvore
     * @param fileName Ficheiro das palavras
     * @return Nó com as palavras do ficheiro
     */
    public static TNode loadWordsFromFile(TNode root, String fileName){
         if(root == null)return null;
         try(Scanner scn = new Scanner(new FileReader(fileName))){
             while (scn.hasNext()) {
                 String word = scn.next();   //Ler palavra
                 int len = 0;
                 TNode curr = root;
                 while (len < word.length()) {
                     int idx = word.charAt(len) - 'a';   //Baseado no caracter obter idx
                     if (curr.children[idx] == null) { //Apenas colocar se não existir
                         curr.children[idx] = new TNode();  //Criar array de filhos
                     }
                     curr = curr.children[idx];  //Passar para os filhos de curr
                     ++len;
                 }
                 curr.isWord = true; //Último nó recebe a tag de ser palavra
             }

             return root;    //Retornar a raiz inicial preenchida
         }
         catch (IOException e){ //Lidar com excepção
             System.out.println("Error loading file:" + e.getMessage());
             return null;
         }
    }

    /**
     * Encontra o nó da árvore n-ária,
     * referenciada por root que contenha o prefixo da palavra prefix.
     * @param root  Inicio da árvore
     * @param prefix Prefixo a procurar
     * @return Nó com o prefixo ou null caso não exista
     */
    public static TNode longestWithPrefix(TNode root, String prefix){
            if(root == null) return null;
            TNode curr = root;
            int len = 0;

            //Percorrer os nós até encontrar o nó com o prefixo
            while(len < prefix.length()){
                int idx = prefix.charAt(len) - 'a';
                if(curr.children[idx] == null) return null; //Não existe esse prefixo na árvore
                curr = curr.children[idx];
                ++len;
            }

            return curr;
    }


    /**
     * O número de palavras que tenham como prefixo a palavra prefix
     * e que existam na árvore n-ária referenciada por root.
     * @param root Inicio da árvore
     * @param prefix Prefixo a procurar
     * @return Número de palavras com o prefixo
     */
    public static int countPossibleWords(TNode root, String prefix){
        if(root == null)return 0;
        TNode ret = longestWithPrefix(root,prefix); //Encontrar o nó com o prefixo

        return countWords(ret);
    }

    //Método auxiliar recursivo para contar palavras
    private static int countWords (TNode root){
        if(root == null) return 0;
        int i = 0;
        int size = 0;
        while (i < root.children.length){
          size += countWords(root.children[i++]);
        }

        return size + (root.isWord ? 1 : 0) ;

    }

}
