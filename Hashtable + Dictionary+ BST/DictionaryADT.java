package data_structures;

import java.util.Iterator;

public interface DictionaryADT<K, V> {
// Adds the given key/value pair to the dictionary. Returns
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
	public boolean put(K key, V value);

// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
	public boolean delete(K key);

// Returns the value associated with the parameter key. Returns
// null if the key is not found or the dictionary is empty.
	public V get(K key);

// Returns the key associated with the parameter value. Returns
// null if the value is not found in the dictionary. If more
// than one key exists that matches the given value, returns the
// first one found.
	public K getKey(V value);

// Returns the number of key/value pairs currently stored
// in the dictionary
	public int size();

// Returns true if the dictionary is full
	public boolean isFull();

// Returns true if the dictionary is empty
	public boolean isEmpty();

// Makes the dictionary empty
	public void clear();

// Returns an Iterator of the keys in the dictionary, in ascending
// sorted order
	public Iterator<K> keys();

// Returns an Iterator of the values in the dictionary. The
// order of the values must match the order of the keys.
	public Iterator<V> values();
}
