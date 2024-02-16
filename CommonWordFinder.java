import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Program to find the most common
 * word within a text file.
 * @author Leon Gruber, ldg2134
 * @version 1.0 December 18, 2021
 */

public class CommonWordFinder {

    private static int limit;
    private static MyMap<String,Integer> map;
    private static Entry<String,Integer>[] array;


    @SuppressWarnings("unchecked")
    public static Entry<String,Integer>[] sorter(MyMap<String,Integer> map,boolean hash){

        Entry<String,Integer>[] array = new Entry[map.size()];
        Iterator<Entry<String,Integer>> iterator = map.iterator();
        Entry<String,Integer> item;
        int count = 0;

        for(int i = 0;i<map.size();i++){
            item = iterator.next();
            array[count++] = item;
        }
        // Separate sorting methods for hash and trees.
        if(hash){
            array = sorter_hash(array);
        }
        else{
            array = sorter_tree(array);
        }
        return array;
    }


    private static Entry<String,Integer>[] sorter_tree(Entry<String,Integer>[] array) {
        // The nested for loop structure acts as a modified insertion sort,
        // sorting the entries in descending order.
        for (int pointer = array.length - 1; pointer > 0; pointer--) {
            for (int j = pointer; j < array.length; j++) {
                if (array[j - 1].value < array[j].value) {
                    Entry<String, Integer> temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                } else {
                    break;
                }
            }
        }
        return array;
    }


    private static Entry<String,Integer>[] sorter_hash(Entry<String,Integer>[] array) {
        for (int pointer = array.length - 1; pointer > 0; pointer--) {
            for (int j = pointer; j < array.length; j++) {
                if (array[j - 1].value < array[j].value) {
                    Entry<String, Integer> temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;

            // This else if clause is needed specifically for the hash table,
            // as entries, unlike the trees, are not stored in alphabetized order.
                } else if (array[j - 1].key.compareTo(array[j].key) > 0
                        && array[j - 1].value == array[j].value) {
                    Entry<String, Integer> temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
                else {
                    break;}
            }
        }
        return array;
    }


    public static String printer(Entry<String,Integer>[] array,int limit){
        StringBuilder builder = new StringBuilder();
        // Limit may not exceed array length.
        int lim = limit>array.length ? array.length : limit;
        int max_string = 0;
        for(int i = 0;i<lim;i++){
            if(array[i].key.length() > max_string){
                max_string = array[i].key.length();}
        }

        // Formatting
        for(int i = 1;i <= lim; i++){
            int white_spaces = Integer.toString(lim).length() - Integer.toString(i).length();
            String spaces = " ".repeat(white_spaces);

            builder.append(spaces+i+". "+array[i-1].key);

            String space = " ".repeat(max_string-array[i-1].key.length()+1);

            builder.append(space+array[i-1].value+System.lineSeparator());
        }
        return builder.toString();
    }


    private static StringBuilder check_char(MyMap<String,Integer> map, int input,StringBuilder word){
        Integer return_val;
        char ch = (char) input;

        if(ch == ' ' || ch == '\n' || ch == '\r'){
            if(word.length()>0){
                // Appends word with incremented value (by 1)
                if((return_val = map.get(word.toString()))!=null){
                    map.put(word.toString(),return_val+1);
                }
                // Appends word if seen for first time
                else{
                    map.put(word.toString(),1);
                }
                // Clear string builder after word has been appended.
                word.setLength(0);

                return word;
            }
            return word;
        }

        ch = Character.toLowerCase(ch);
        if((ch >= 'a' && ch <= 'z') || (ch=='\'')){
            word.append(ch);
        }

        if(ch == '-' && word.length() > 0){
            if(word.length() != 0){
                word.append(ch);
            }
        }
        return word;
    }


    public static void main (String[] args){
        // Checking for correct command line input
        if(args.length < 2 || args.length > 3){
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }
        if(args[1].equals("bst")){
            map = new BSTMap<>();
        }
        else if(args[1].equals("avl")){
            map = new AVLTreeMap<>();
        }
        else if(args[1].equals("hash")){
            map = new MyHashMap<>();
        }
        else{
            System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
            System.exit(1);
        }

        if(args.length>2){
            try{
                if(Integer.parseInt(args[2])<0){
                    System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                    System.exit(1);
                }
                limit = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException e){
                System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(1);
            }
        }
        else{
            limit = 10;
        }

        // Actual execution of the main program
        try {
            int ch;
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            StringBuilder word = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                word = check_char(map,ch,word);
            }
            // Append last word if txt file does not end on space/new line
            if(word.length() != 0){
                Integer return_val;
                if((return_val = map.get(word.toString()))!=null){
                    map.put(word.toString(),return_val+1);
                }
                else{
                    map.put(word.toString(),1);
                }
            }

        }
        catch(IOException e){
            System.err.println("Error: An I/O error occurred reading '" + args[0] + "'.");
        }

        if(args[1].equals("hash")){
            array = sorter(map,true);
        }
        else{
            array = sorter(map,false);
        }

        System.out.println("Total unique words: "+array.length);
        System.out.print(printer(array,limit));

    }
}
