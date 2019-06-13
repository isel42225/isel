package serie3.Pt1;


import java.util.Comparator;


public class TreeUtils {

    static class Node<E> {
        E value;
        Node<E> left, right;

        Node (E v){ value = v;}
    }

    public static <E> boolean contains(Node<E> root, E min, E max, Comparator<E> cmp){

        while(root != null){
            int cmpMin = cmp.compare(root.value, min);
            int cmpMax = cmp.compare(root.value, max);
            if(cmpMin >= 0 && cmpMax <= 0) return true;
            if(cmpMin < 0) root = root.right;
            else
                root = root.left;
        }

        return false;

    }

    public static <E extends Comparable<E>> Node<E> lowestCommonAncestor(Node<E> root, E n1, E n2){

        int cmp1 = root.value.compareTo(n1);
        int cmp2 = root.value.compareTo(n2);

        if(cmp1 > 0 && cmp2 > 0 ) return lowestCommonAncestor(root.left, n1,n2);
        if(cmp1 < 0 && cmp2 < 0 ) return lowestCommonAncestor(root.right,n1,n2);
        return root;
    }

    /**
     * Dizer se a arvore esta balanceada.
     * @param root Inicio da árvore
     * @param <E> Tipo dos dados da árvore
     */
        public static <E> boolean isBalanced(Node<E> root) {
        return root != null && checkBalance(root) != -1;
        }

        private static <T> int checkBalance(Node<T> node){
            if(node == null) return 0;
            int left = checkBalance(node.left);

            if(left == -1) return -1;

            int right = checkBalance(node.right);

            if(right == -1) return -1;

            if(Math.abs(left - right) > 1){
                return -1;  //Diferença de alturas das sub arvores maior que 1
            }else{
                return 1 + Math.max(left, right); //Retornar a altura da arvore
            }


        }


}
