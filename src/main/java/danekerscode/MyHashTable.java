package danekerscode;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class MyHashTable<K, V> {

    private static class HashNode<K, V> {
        private final K key;
        private V value;
        private HashNode<K, V> next;

        public HashNode(K key, V value) {
            if (key == null || value == null)
                throw new NullPointerException(("my hash table does not take null params"));

            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return "{" + key + "=" + value + "}";
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

    }

    private LinkedList<HashNode<K, V>>[] buckets;
    private int M = 11; // default amount of buckets
    private int size;


    public MyHashTable() {
        initialize(this.M);
    }

    public MyHashTable(int capacity) {
        this.M = capacity;
        initialize(capacity);
    }

    private void initialize(int length) {
        this.buckets = new LinkedList[length];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int findIndex(K key) {
        return hash(key) % this.buckets.length;
    }

    private int hash(K key) {
        return key.hashCode();
    }

    public void put(K key, V value) {
        // difference between HashTable and HashMap
        // HashTable doesn't support null keys and values and methods not synchronized & HashTable values/keys will return as enums
        // HashMap methods synchronized && HashMap can take one null key and infinity amount of null values & HashMap values/keys will return as sets

        if (this.M != 11 && this.size() > this.M) { // checking => current size >? created capacity
            throw new ArrayIndexOutOfBoundsException("max capacity is: " + this.M);
        }


        HashNode<K, V> hashNodeToPut = new HashNode<>(key, value);
        LinkedList<HashNode<K, V>> list = findList(key);
        System.out.println(list);
        list.add(hashNodeToPut);
        size++;
    }

    public LinkedList<HashNode<K, V>> findList(K key) {
        return this.buckets[this.findIndex(key)];
    }

    private void increaseCurrentTableSize() {
        int newSize = size() * 2;
        LinkedList<HashNode<K, V>>[] increased = new LinkedList[newSize];

        System.arraycopy(this.buckets, 0, increased, 0, size()-1);

        for (int i = size(); i < newSize; i++) {
            increased[i] = new LinkedList<>();
        }

        this.buckets = increased;
    }


    public V get(K key) {
        return findList(key) // find which located this key
                .stream() // Stream API added in Java 8
                .filter(node -> node.key.equals(key)) // searching elements which support this condition
                .map(HashNode::getValue) // convert HashNode to HashNode.getValue && :: <- reference method
                .findFirst()  // if we find HashNode with this key we will return value of this HashNode
                .orElse(null); //  otherwise null
    }

    public Boolean contains(K key) {
        return this.get(key) != null;
    }

    public V remove(K key) {
        LinkedList<HashNode<K, V>> list = findList(key);
        HashNode<K, V> hashNode =
                list
                        .stream()
                        .filter(node -> node.key.equals(key))
                        .findFirst()
                        .orElseThrow(() -> new NullPointerException("this map does not contains this key: " + key.toString()));

        list.removeIf(node -> node.equals(hashNode));
        size--;

        return hashNode.getValue();
    }

    public K getKey(V value) {
        return Arrays
                .stream(this.buckets)
                .filter(list -> list.contains(value))
                .findFirst()
                .map(list -> list.get(0).key)
                .orElseThrow(() -> new NullPointerException("this map does not contains this value: " + value.toString()));
    }

    @Override
    public String toString() {
        return "[" +
                Arrays.stream(this.buckets)
                        .filter( arr -> arr!=null && arr.size() > 0) // collecting list with size greater than 0
                        .map(temp -> {
                            String str = Arrays.toString(new LinkedList[]{temp});
                            return str.substring(2, str.length() - 2); // converting to normal form to display
                        })
                        .collect(Collectors.joining(",")) + "]"; // adding comma between elements

    }

    public int size() {
        return this.size;
    }


    public void printAmountOfBucketsInEachList(){
        for (LinkedList<HashNode<K,V>> linkedList : this.buckets){
            System.out.println(linkedList.size());
        }
    }


}
