import java.util.Iterator;

/**
 * Class for a simple hash map.
 * @author Leon Gruber
 * @version 1.0 November 19, 2021
 */
public class MyHashMap<K extends Comparable<K>, V> implements MyMap<K, V> {
    // Helpful list of primes available at:
    // https://www2.cs.arizona.edu/icon/oddsends/primes.htm
    private static final int[] primes = new int[] {
            101, 211, 431, 863, 1733, 3467, 6947, 13901, 27803, 55609, 111227,
            222461 };
    private static final double MAX_LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int primeIndex, numEntries;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[primes[primeIndex]];
    }


    public int getTableSize() {
        return table.length;
    }


    @Override
    public int size() {
        return numEntries;
    }


    @Override
    public boolean isEmpty() {
        return size()>0 ? false : true;
    }


    @Override
    public V get(K key) {
        if(key==null){
            return null;
        }
        int index = key.hashCode()%table.length;
        Entry<K,V> item = table[index];
        while(item!=null){
            if (item.key.equals(key)){
                return item.value;
            }
            item=item.next;
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        int index = key.hashCode()%table.length;
        Entry<K,V> insert = new Entry<K,V>(key,value);
        Entry<K,V> item = table[index];

        while(item!=null){
            if (item.key.equals(key)){
                V oldValue = item.value;
                item.value = value;
                return oldValue;
            }
            item=item.next;
        }
        if(table[index]==null){
            table[index] = insert;
        }
        else {
            item = table[index];
            table[index] = insert;
            insert.next = item;
        }
        numEntries++;

        if(getLoadFactor()>MAX_LOAD_FACTOR && primes[primeIndex] < 222461){
            rehash();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K,V>[] newTable = new Entry[primes[++primeIndex]];
        numEntries=0;
        for(Entry<K,V> entry : table){
            while(entry!=null){
                putRehash(entry.key,entry.value,newTable);
                entry=entry.next;}
        }
        table = newTable;
    }
    private void putRehash(K key,V value,Entry<K,V>[] list){
        int index = key.hashCode()%list.length;
        Entry<K,V> insert = new Entry<K,V>(key,value);
        Entry<K,V> item = list[index];

        if(list[index]==null){
            list[index] = insert;
            numEntries++;
        }
        else{
            item = list[index];
            list[index] = insert;
            insert.next = item;
            numEntries++;}
    }


    @Override
    public V remove(K key) {
        if(key==null){
            return null;
        }
        int index = key.hashCode()%table.length;
        Entry<K,V> remove = table[index];
        if(remove==null){
            return null;
        }

        if (remove.next == null && remove.key.equals(key)) {
            table[index] = null;
            numEntries--;
            return remove.value;
        }
        else if(remove.next != null && remove.key.equals(key)){
            table[index] = remove.next;
            numEntries--;
            return remove.value;
        }
        else {
            Entry<K,V> previous = remove;
            remove=remove.next;
            while (remove != null) {
                if (remove.key.equals(key)) {
                    previous.next = remove.next;
                    numEntries--;
                    return remove.value;
                }
                remove = remove.next;
            }
        }
        return null;
    }


    public double getLoadFactor() {
        return (double)numEntries / primes[primeIndex];
    }


    public int computeMaxChainLength() {
        int maxChainLength = 0;
        for (Entry<K, V> chain : table) {
            if (chain != null) {
                int currentChainLength = 0;
                Entry<K, V> chainPtr = chain;
                while (chainPtr != null) {
                    currentChainLength++;
                    chainPtr = chainPtr.next;
                }
                if (currentChainLength > maxChainLength) {
                    maxChainLength = currentChainLength;
                }
            }
        }
        return maxChainLength;
    }


    public String toString() {
        if (numEntries > 1000) {
            return "HashMap too large to represent as a string.";
        }
        if (numEntries == 0) {
            return "HashMap is empty.";
        }
        int maxIndex;
        for (maxIndex = table.length - 1; maxIndex >= 0; maxIndex--) {
            if (table[maxIndex] != null) {
                break;
            }
        }
        int maxIndexWidth = String.valueOf(maxIndex).length();
        StringBuilder builder = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> chain = table[i];
            if (chain != null) {
                int indexWidth = String.valueOf(i).length();
                builder.append(" ".repeat(maxIndexWidth - indexWidth));
                builder.append(i);
                builder.append(": ");
                while (chain != null) {
                    builder.append(chain);
                    if (chain.next != null) {
                        builder.append(" -> ");
                    }
                    chain = chain.next;
                }
                builder.append(newLine);
            }
        }
        return builder.toString();
    }

   
    public Iterator<Entry<K, V>> iterator() {
        return new MapItr();
    }

    private class MapItr implements Iterator<Entry<K, V>> {
        private Entry<K, V> current;
        private int index;

        MapItr() {
            advanceToNextEntry();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Entry<K, V> next() {
            Entry<K, V> e = current;
            if (current.next == null) {
                index++;
                advanceToNextEntry();
            } else {
                current = current.next;
            }
            return e;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void advanceToNextEntry() {
            while (index < table.length && table[index] == null) {
                index++;
            }
            current = index < table.length ? table[index] : null;
        }
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        int upperLimit = 100;
        int expectedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            map.put(String.valueOf(i), i);
            expectedSum += i;
        }
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);


        int receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.get(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);

        expectedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            int newValue = upperLimit - i + 1;
            map.put(String.valueOf(i), newValue);
            expectedSum += newValue;
        }
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);

        receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.get(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);

        receivedSum = 0;
        Iterator<Entry<String, Integer>> iter = map.iterator();
        while (iter.hasNext()) {
            receivedSum += iter.next().value;
        }
        System.out.println("Received sum: " + receivedSum);

        receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.remove(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);
    }
}
