package serie2.Pt1;

import stack.LinkedStack;

public class Utils {

    public static boolean verifyXML(String str){

        if(str.length()== 0)return true;

        LinkedStack<String> tagStack = new LinkedStack<>();
        boolean found = false;
        int currIdx = 0;
        while(str.length() > 0 && currIdx < str.length()){
            if(str.charAt(currIdx) == '<'){
                String s = str.substring((currIdx + 1), str.indexOf('>',currIdx));
                if(s.charAt(0) != '/'){
                    if (!isName(s))return false;
                    tagStack.push(s);
                    found = true;
                }
                else {
                    if (tagStack.isEmpty()) return false;
                    s = s.substring(1);
                    if (!isName(s) || !(s.equals(tagStack.peek()))) return false;
                    tagStack.pop();
                }
                str = str.substring(str.indexOf('>')+1);
                currIdx = 0;
            }
            else {
                ++currIdx;
            }
        }

        return tagStack.isEmpty() && found;
    }

    private static boolean isName(String s) {
        boolean b = true;

       for(int i = 1;i<s.length()-1 && b;++i ){
           b = (s.charAt(i) >= 'A' && s.charAt(i) <= 'z') || (s.charAt(i) >= '0' && s.charAt(i) <= '9');
        }
        return b;
    }

}
