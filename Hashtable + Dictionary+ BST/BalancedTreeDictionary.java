/**
 * Program #4
 * Balanced Tree Dictionary w/Red Black Tree
 * CS310
 * 4/30/2020
 * @author Matthew Turi cssc1284
 */
package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTreeDictionary<K, V extends Comparable<V>> implements DictionaryADT<K, V> {
	private int modCounter, currentSize;
	private TreeMap<K, V> btd;

	public BalancedTreeDictionary() {
		btd = new TreeMap<K, V>();
		currentSize = 0;
		modCounter = 0;
	}

	@Override
	public boolean put(K key, V value) {
		try {
			btd.put(key, value);
		} catch (Exception e) {
			return false;
		}
		currentSize++;
		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		if (btd.descendingKeySet().contains(key)) {
			try {
				btd.remove(key);
			} catch (Exception e) {
				return false;
			}
			currentSize--;
			modCounter++;
			return true;
		}
		return false;
	}

	@Override
	public V get(K key) {
		try {
			return (btd.get(key));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public K getKey(V value) {
		if (btd.values().contains(value)) {
			Iterator<V> vals = btd.values().iterator();
			Iterator<K> keys = btd.keySet().iterator();
			while (vals.hasNext()) {
				V val = vals.next();
				K key = keys.next();
				if (val.compareTo(value) == 0) {
					return key;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public void clear() {
		btd.clear();
		currentSize = 0;
		modCounter++;
	}

	@Override
	public Iterator<K> keys() {
		return btd.keySet().iterator();
	}

	@Override
	public Iterator<V> values() {
		return btd.values().iterator();
	}

}
