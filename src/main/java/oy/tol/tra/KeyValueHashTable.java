package oy.tol.tra;

public class KeyValueHashTable<K extends Comparable<K>, V> implements Dictionary<K, V> {

    // This should implement a hash table.

    private Pair<K, V>[] values = null;
    private int count = 0;
    private int collisionCount = 0;
    private int maxProbingSteps = 0;
    private int reallocationCount = 0;
    private static final double LOAD_FACTOR = 0.45;
    private static final int DEFAULT_CAPACITY = 20;

    public KeyValueHashTable(int capacity) throws OutOfMemoryError {
        ensureCapacity(capacity);
    }

    public KeyValueHashTable() throws OutOfMemoryError {
        ensureCapacity(DEFAULT_CAPACITY);
    }

    @Override
    public Type getType() {
        return Type.HASHTABLE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void ensureCapacity(int capacity) throws OutOfMemoryError {
        if (capacity < DEFAULT_CAPACITY) {
            capacity = DEFAULT_CAPACITY;
        }
        values = (Pair<K, V>[]) new Pair[tableSizeFor(capacity)];
        reallocationCount = 0;
        count = 0;
        collisionCount = 0;
        maxProbingSteps = 0;
    }

    @Override
    public int size() {
        return count;
    }

    /**
     * Prints out the statistics of the hash table.
     * Here you should print out member variable information which tell something
     * about your implementation.
     * <p>
     * For example, if you implement this using a hash table, update member
     * variables of the class (int counters) in add() whenever a collision
     * happen. Then print this counter value here.
     * You will then see if you have too many collisions. It will tell you that your
     * hash function is not good.
     */
    @Override
    public String getStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Hash table load factor is %.2f%n", LOAD_FACTOR));
        builder.append(String.format("Hash table capacity is %d%n", values.length));
        builder.append(String.format("Current fill rate is %.2f%%%n", (count / (double)values.length) * 100.0));
        builder.append(String.format("Hash table had %d collisions when filling the hash table.%n", collisionCount));
        builder.append(String.format("Hash table had to probe %d times in the worst case.%n", maxProbingSteps));
        builder.append(String.format("Hash table had to reallocate %d times.%n", reallocationCount));
        return builder.toString();
    }

    @Override
    public boolean add(K key, V value) throws IllegalArgumentException, OutOfMemoryError {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }
        int index = indexFor(key);
        int steps = 0;

        while (steps < values.length) {
            Pair<K, V> pair = values[index];
            if (pair == null) {
                if (count + 1 > values.length * LOAD_FACTOR) {
                    reallocate(values.length * 2);
                    return add(key, value);
                }
                values[index] = new Pair<>(key, value);
                count++;
                if (steps > 0) {
                    collisionCount += steps;
                    if (steps > maxProbingSteps) {
                        maxProbingSteps = steps;
                    }
                }
                return true;
            }
            if (pair.getKey().equals(key)) {
                pair.setValue(value);
                return true;
            }
            steps++;
            index = (index + 1) % values.length;
        }
        reallocate(values.length * 2);
        return add(key, value);
    }

    @Override
    public V find(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        int index = indexFor(key);
        int steps = 0;
        while (steps < values.length) {
            Pair<K, V> pair = values[index];
            if (pair == null) {
                return null;
            }
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
            steps++;
            index = (index + 1) % values.length;
        }
        return null;
    }

    @Override
    @java.lang.SuppressWarnings({"unchecked"})
    public Pair<K,V> [] toSortedArray() {
        Pair<K, V> [] sorted = (Pair<K,V>[])new Pair[count];
        int newIndex = 0;
        for (int index = 0; index < values.length; index++) {
           if (values[index] != null) {
              sorted[newIndex++] = new Pair<>(values[index].getKey(), values[index].getValue());
           }
        }
        Algorithms.fastSort(sorted);
        return sorted;
      }

    @SuppressWarnings("unchecked")
    private void reallocate(int newSize) throws OutOfMemoryError {
        if (newSize < tableSizeFor(DEFAULT_CAPACITY)) {
            newSize = tableSizeFor(DEFAULT_CAPACITY);
        }
        reallocationCount++;
        Pair<K, V>[] oldPairs = values;
        this.values = (Pair<K, V>[]) new Pair[newSize];
        count = 0;
        collisionCount = 0;
        maxProbingSteps = 0;
        for (int index = 0; index < oldPairs.length; index++) {
            if (oldPairs[index] != null) {
                add(oldPairs[index].getKey(), oldPairs[index].getValue());
            }
        }
    }

    @Override
    public void compress() throws OutOfMemoryError {
        int newCapacity = tableSizeFor(count);
		    if (newCapacity < values.length) {
		      reallocate(newCapacity);
		    } 
    }

    private int indexFor(K key) {
        return Math.floorMod(key.hashCode(), values.length);
    }

    private static int tableSizeFor(int elementCapacity) {
        return (int)Math.ceil(elementCapacity / LOAD_FACTOR);
    }
 
}
