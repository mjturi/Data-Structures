/**
 * Program #4
 * Hashtable w/chaining
 * CS310
 * 4/30/2020
 * @author Matthew Turi cssc1284
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable<K extends Comparable<K>, V extends Comparable<V>> implements DictionaryADT<K, V> {
	private int modCounter, currentSize, maxSize;
	private ListADT<DictionaryNode<K, V>>[] list;

	/**
	 * Method to take a key and generate a hashvalue that will fit into the
	 * hashtable given the current array size
	 * 
	 * @param key
	 * @return int (hash value)
	 */
	private int map(K key) {
		int hashCode = key.hashCode();
		int index = hashCode % maxSize;
		return index;
	}

	/**
	 * Wrapper class for nodes within the table, taken from Riggins CourseReader
	 * 
	 * @author Alan Riggins
	 * @param <K> generic key
	 * @param <V> generic value
	 */
	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>> {
		K key;
		V value;

		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public int compareTo(DictionaryNode<K, V> node) {
			return ((Comparable<K>) key).compareTo((K) node.key);
		}

	}

	/**
	 * Hashtable constructor taken from CourseReader, sets a maxSize to keep track
	 * of array indeces, fills each array index with a linked list of
	 * DictionaryNodes, and initializes modCounter and currentSize
	 * 
	 * @param size
	 */
	public Hashtable(int size) {
		maxSize = size;
		list = new LinkedListDS[size];
		for (int i = 0; i < size; i++) {
			list[i] = new LinkedListDS<DictionaryNode<K, V>>();
		}
		modCounter = 0;
		currentSize = 0;
	}

	@Override
	public boolean put(K key, V value) {
		int index = map(key);
		Iterator<Hashtable<K, V>.DictionaryNode<K, V>> iter = list[index].iterator();
		DictionaryNode<K, V> temp = new DictionaryNode(key, value);
		if (iter.hasNext()) {
			DictionaryNode<K, V> head = iter.next();
			while (iter.hasNext()) {
				if (head.compareTo(temp) == 0) {
					return false;
				}
				head = iter.next();
			}
		}
		list[index].addFirst(temp);
		currentSize++;
		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		int index = map(key);
		DictionaryNode<K, V> temp = new DictionaryNode(key, null);
		if (list[index].remove(temp) == true) {
			currentSize--;
			modCounter++;
			return true;
		}
		return false;
	}

	@Override
	public V get(K key) {
		int index = map(key);
		Iterator<Hashtable<K, V>.DictionaryNode<K, V>> iter = list[index].iterator();
		if (iter.hasNext()) {
			DictionaryNode<K, V> temp = iter.next();
			DictionaryNode<K, V> trial = new DictionaryNode(key, null);
			if (temp.compareTo(trial) == 0) {
				return temp.value;
			}
			while (iter.hasNext()) {
				temp = iter.next();
				if (temp.compareTo(trial) == 0)
					return temp.value;
			}
		}
		return null;
	}

	@Override
	public K getKey(V value) {
		for (int i = 0; i < maxSize; i++) {
			Iterator<Hashtable<K, V>.DictionaryNode<K, V>> iter = list[i].iterator();
			while (iter.hasNext()) {
				DictionaryNode<K, V> temp = iter.next();
				if (temp.value.compareTo(value) == 0) {
					return temp.key;
				}
			}
		}
		return null;
	}

	@Override
	public void clear() {
		for (int i = 0; i < maxSize; i++) {
			list[i].makeEmpty();
		}
		currentSize = 0;
		modCounter++;
	}

	@Override
	public Iterator<K> keys() {
		return new keyIter();
	}

	class keyIter implements Iterator<K> {
		private int modificationCounter;
		private int iterIndex;
		private LinkedListDS<DictionaryNode<K, V>> keys;
		private DictionaryNode<K, V> last;

		public keyIter() {
			iterIndex = 0;
			modificationCounter = modCounter;
			keys = new LinkedListDS<DictionaryNode<K, V>>();
			last = null;
			fillKeys();
		}

		@Override
		public boolean hasNext() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		@Override
		public K next() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			if (!hasNext())
				throw new NoSuchElementException();
			return nextKey(last);
		}

		/**
		 * fillKeys() creates a linked list iterator for every index of the array and
		 * adds all (if any) elements in that chain to a seperate linked list, "keys"
		 */
		private void fillKeys() {
			for (int i = 0; i < maxSize; i++) {
				if (list[i].size() > 0) {
					Iterator<Hashtable<K, V>.DictionaryNode<K, V>> iter = list[i].iterator();
					while (iter.hasNext()) {
						DictionaryNode<K, V> curr = iter.next();
						keys.addLast(curr);
					}
				}

			}
		}

		/**
		 * nextKey keeps track of the last Node returned and will continually return the
		 * next lowest key from the keys linked list
		 * 
		 * @param last: node that was just returned by the iterator
		 * @return K key of next in-order node
		 */
		private K nextKey(DictionaryNode<K, V> last) {
			Iterator<DictionaryNode<K, V>> iter = keys.iterator();
			DictionaryNode<K, V> min = null;
			DictionaryNode<K, V> curr;
			while (iter.hasNext()) {
				curr = iter.next();
				if (min == null || min.compareTo(curr) > 0) {
					if (last == null || curr.compareTo(last) > 0) {
						min = curr;
					}
				}
			}
			this.last = min;
			iterIndex++;
			return min.key;
		}
	}

	@Override
	public Iterator<V> values() {
		return new valIter();
	}

	class valIter implements Iterator<V> {
		int modificationCounter;
		private LinkedListDS<DictionaryNode<K, V>> vals;
		private DictionaryNode<K, V> last;
		int iterIndex;

		public valIter() {
			vals = new LinkedListDS<DictionaryNode<K, V>>();
			last = null;
			iterIndex = 0;
			modificationCounter = modCounter;
			fillVals();
		}

		@Override
		public boolean hasNext() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		@Override
		public V next() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			if (!hasNext())
				throw new NoSuchElementException();
			return nextVal(last);
		}

		/**
		 * fillVals() creates a linked list iterator for every index of the array and
		 * adds all (if any) elements in that chain to a seperate linked list, "vals"
		 */
		private void fillVals() {
			for (int i = 0; i < maxSize; i++) {
				if (list[i].size() > 0) {
					Iterator<Hashtable<K, V>.DictionaryNode<K, V>> iter = list[i].iterator();
					while (iter.hasNext()) {
						DictionaryNode<K, V> curr = iter.next();
						vals.addLast(curr);
					}
				}

			}
		}

		/**
		 * nextVal keeps track of the last Node returned and will continually return the
		 * next lowest value from the keys linked list
		 * 
		 * @param last: node that was just returned by the iterator
		 * @return V value of next in-order key
		 */
		private V nextVal(DictionaryNode<K, V> last) {
			Iterator<DictionaryNode<K, V>> iter = vals.iterator();
			DictionaryNode<K, V> min = null;
			DictionaryNode<K, V> curr;
			while (iter.hasNext()) {
				curr = iter.next();
				if (min == null || min.compareTo(curr) > 0) {
					if (last == null || curr.compareTo(last) > 0) {
						min = curr;
					}
				}
			}
			this.last = min;
			iterIndex++;
			return min.value;
		}
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public int size() {
		return currentSize;
	}

}
