public class elementary_sorts {
    /**
     * Returns index of the maximum element in array[0..n-1].
     */
    static int operations;


    public static void selection_sort(int[] array){
        operations = 0;
        for (int i = 0;i<array.length-1;i++){
            int min = i+1;
            for(int j = i+1;j<array.length;j++){
                if(array[j]<array[min]) {
                    min = j;
                }
                operations++;
            }
            if(array[min]<array[i]){
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;}

        }
    }

    public static void insertion_sort(int[] array){
        operations = 0;
        for(int pointer = 1; pointer<array.length;pointer++){
            for(int i = pointer;i>0;i--){
                operations++;
                if(array[i]<array[i-1]) {
                    int temp = array[i];
                    array[i] = array[i-1];
                    array[i-1] = temp;
                }
                else{
                    break;
                }
            }
        }
    }

    public static void bubble_sort(int[] array){
        operations = 0;
        int length = array.length;
        for(int i=0;i<length;length--){
            for(int j = 1;j<length;j++){
                if(array[j]<array[j-1]){
                    int temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }
                operations++;
            }
        }
    }

    public static void quicksort(int[] array){
        operations = 0;
        quicksort_helper(array,0,array.length-1);
    }

    public static void quicksort_helper(int[] array,int left,int right){
        if(left<right) {
            int pivot = lomuto(array, left, right);
            quicksort_helper(array,left,pivot-1);
            quicksort_helper(array,pivot+1,right);
        }

    }

    public static int lomuto(int[] array,int left,int right){
        int p = left+1;
        int i;
        for (i = p;i<=right;i++){
            if(array[i]<array[left]){
                p++;
                if(p!=i){
                    int temp = array[p];
                    array[i] = array[p];
                    array[p] = temp;
                }
                operations++;
            }
            int temp = array[left];
            array[left] = array[i];
            array[i] = temp;
        }
        return i;
    }

    public static void main (String[] args){
        int[] array = {3,4,1,6,5,2,3,4,44,421,12,42,8,9,456,187,23};
        int[] array2 = {3,4,1,6,5,2,3,4,44,421,12,42,8,9,456,187,23};
        int[] array3 = {3,4,1,6,5,2,3,4,44,421,12,42,8,9,456,187,23};
        int[] array4 = {23,445,383,43,1,98,64,20,3,4,1,6,5,2,3,4,44,421,12,42,8,9,456,187,23};
        selection_sort(array);
        System.out.println("Selection: "+operations+", N = "+array.length);
        for(int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("Insertion: " + operations+", N = "+array2.length);
        insertion_sort(array2);
        for(int i : array2){
            System.out.print(i+" ");
        }
        System.out.println();
        System.out.println("Bubble operations: "+ operations+", N = "+array3.length);
        bubble_sort(array3);
        for(int i : array3){
            System.out.print(i+" ");
        }
        System.out.println();
        System.out.println("Quicksort operations: "+ operations+", N = "+array4.length);
        bubble_sort(array4);
        for(int i : array4){
            System.out.print(i+" ");
        }
    }

}
