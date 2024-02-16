import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with two different methods to count inversions in an array of integers.
 * @author Leon Gruber ldg2134
 * @version 1.0.0 November 17, 2021
 */
public class InversionCounter {

    private static long counter;

    public static long countInversionsSlow(int[] array) {
        long count = 0;

        for(long i=0;i<array.length;i++){
            for(long j=i;j<array.length;j++){
                if(array[(int)i]>array[(int)j]){
                    count++;}
            }
        }
        return count;
    }

    public static long countInversionsFast(int[] array) {
        // Make a copy of the array so you don't actually sort the original
        // array.
        int[] arrayCopy = new int[array.length],
              scratch =  new int[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);
        // TODO - fix return statement
        counter = 0;
        mergesortHelper(arrayCopy,scratch,0,array.length-1);
        return counter;
    }


    private static void mergesortHelper(int[] array, int[] scratch, int low, int high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergesortHelper(array, scratch, low, mid);
            mergesortHelper(array, scratch, mid + 1, high);
            int i = low, j = mid + 1;
            for (int k = low; k <= high; k++) {
                if (i <= mid && (j > high || array[i] <= array[j])) {
                    scratch[k] = array[i++];
                } else {
                    scratch[k] = array[j++];
                    counter += 1+ mid - i;
                }
            }
            for (int k = low; k <= high; k++) {
                array[k] = scratch[k];
            }
        }
    }


    private static int[] readArrayFromStdin() throws IOException,
                                                     NumberFormatException {
        List<Integer> intList = new LinkedList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        int value = 0, index = 0, ch;
        boolean valueFound = false;
        while ((ch = reader.read()) != -1) {
            if (ch >= '0' && ch <= '9') {
                valueFound = true;
                value = value * 10 + (ch - 48);
            } else if (ch == ' ' || ch == '\n' || ch == '\r') {
                if (valueFound) {
                    intList.add(value);
                    value = 0;
                }
                valueFound = false;
                if (ch != ' ') {
                    break;
                }
            } else {
                throw new NumberFormatException(
                        "Invalid character '" + (char)ch +
                        "' found at index " + index + " in input stream.");
            }
            index++;
        }

        int[] array = new int[intList.size()];
        Iterator<Integer> iterator = intList.iterator();
        index = 0;
        while (iterator.hasNext()) {
            array[index++] = iterator.next();
        }
        return array;
    }

    public static void main(String[] args) {

        if(args.length>1){
            System.err.println("Usage: java InversionCounter [slow]");
            System.exit(1);
        }
        else if(args.length!=0){
            if(!args[0].equals("slow")){
            System.err.println("Error: Unrecognized option '"+args[0]+"'.");
            System.exit(1);}
        }

        try{
            System.out.print("Enter sequence of integers, each followed by a space: ");
            int[] array = readArrayFromStdin();
            if(array.length==0){
                System.err.println("Error: Sequence of integers not received.");
                System.exit(1);
            }

            if(args.length!=0&&args[0]=="slow"){
                System.out.println("Number of inversions: "+countInversionsSlow(array));
            }
            else{
                System.out.println("Number of inversions: "+countInversionsFast(array));
            }

        }
        catch(Exception e){
            System.err.println("Error: "+e.getMessage());
            System.exit(1);}
        }


}
