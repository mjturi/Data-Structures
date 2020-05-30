/**
 * Program #4
 * Binary Search Tree w/LinkedListDS
 * CS310
 * 4/30/2020
 * @author Matthew Turi cssc1284
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K extends Comparable<K>, V extends Comparable<V>> implements DictionaryADT<K, V> {
	private int modCounter, currentSize;
	private ListADT<DictionaryNode<K, V>> bst;
	private DictionaryNode<K, V> root;

	/**
	 * DictionaryNode wrapper class from course reader with added left and right
	 * fields for each node
	 * 
	 * @author Alan Riggins
	 * @param <K> key
	 * @param <V> value
	 */
	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>> {
		K key;
		V value;
		DictionaryNode<K, V> left, right;

		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
			left = null;
			right = null;
		}

		public int compareTo(DictionaryNode<K, V> node) {
			return ((Comparable<K>) key).compareTo((K) node.key);
		}

	}

	public BinarySearchTree() {
		currentSize = 0;
		modCounter = 0;
		bst = new LinkedListDS<DictionaryNode<K, V>>();
		root = null;
	}

	/**
	 * getSuccessor returns the in-order successor to a given node
	 * 
	 * @param curr: the node that needs to be replaced
	 * @return DictionaryNode<K,V> successor
	 */
	private DictionaryNode<K, V> getSuccessor(DictionaryNode<K, V> curr) {
		DictionaryNode<K, V> temp = curr;
		while (temp.left != null) {
			temp = temp.left;
		}
		return temp;
	}

	@Override
	public boolean put(K key, V value) {
		DictionaryNode<K, V> temp = new DictionaryNode(key, value);
		if (isEmpty()) {
			root = temp;
			bst.addFirst(temp);
			currentSize++;
			return true;
		}
		DictionaryNode<K, V> curr = root;
		DictionaryNode<K, V> prev = root;

		while (curr != null) {
			prev = curr;
			if (temp.compareTo(curr) > 0) {
				curr = curr.right;
			} else if (temp.compareTo(curr) < 0) {
				curr = curr.left;
			} else if (temp.compareTo(curr) == 0) {
				return false;
			}
		}
		bst.addLast(temp);
		if (temp.compareTo(prev) < 0)
			prev.left = temp;
		else
			prev.right = temp;
		currentSize++;
		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		/*
		 * As long as the tree isn't empty, we try removing the key from the linked list
		 * and if it returns true (meaning the key was in the LL, then we can
		 * recursively delete it and reset the pointers for the actual tree structure.
		 */
		if (!isEmpty()) {
			DictionaryNode<K, V> temp = new DictionaryNode(key, null);
			if (bst.remove(temp) == true) {
				root = deleteRec(root, key);
			}
			currentSize--;
			modCounter++;
			return true;
		}
		return false;
	}

	/**
	 * deleteRec is a method found online that I modified to work with my structure
	 * It will recur down the tree until it finds the key, and given how many
	 * children the node has, will recursively restructure the tree and return the
	 * correct root with all pointers intact
	 * 
	 * @author GeeksforGeeks
	 * @param root: initially the actual root of the tree, but will recursively
	 *              change to root of subtrees
	 * @param key:  key to be removed
	 * @return DictionaryNode<K,V> root: final return is the real root of the tree,
	 *         will recursively return the roots of all subtrees that were changed
	 */
	private DictionaryNode<K, V> deleteRec(DictionaryNode<K, V> root, K key) {
		if (root == null)
			return root;
		if (key.compareTo(root.key) < 0)
			root.left = deleteRec(root.left, key);
		else if (key.compareTo(root.key) > 0)
			root.right = deleteRec(root.right, key);
		else {
			if (root.left == null)
				return root.right;
			else if (root.right == null)
				return root.left;
			root.key = getSuccessor(root.right).key;
			root.value = getSuccessor(root.right).value;
			root.right = deleteRec(root.right, root.key);
		}
		return root;
	}

	@Override
	public V get(K key) {
		DictionaryNode<K, V> temp = new DictionaryNode<K, V>(key, null);
		DictionaryNode<K, V> curr = root;
		while (curr != null) {
			if (temp.compareTo(curr) > 0) {
				curr = curr.right;
			} else if (temp.compareTo(curr) < 0) {
				curr = curr.left;
			} else {
				return curr.value;
			}
		}
		return null;
	}

	@Override
	public K getKey(V value) {
		DictionaryNode<K, V> curr = root;
		while (curr != null) {
			if (value.compareTo(curr.value) > 0) {
				curr = curr.right;
			} else if (value.compareTo(curr.value) < 0) {
				curr = curr.left;
			} else {
				return curr.key;
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
		bst.makeEmpty();
		currentSize = 0;
		modCounter++;
	}

	@Override
	public Iterator<K> keys() {
		return new keyIter();
	}

	private class keyIter implements Iterator<K> {
		private int modificationCounter;
		private int iterIndex;
		private DictionaryNode<K, V> last;

		public keyIter() {
			iterIndex = 0;
			modificationCounter = modCounter;
			last = null;
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
			return nextKey();
		}

		/**
		 * nextKey creates an iterator over the linkedlist structure without looking at
		 * the tree pointers and returns the next ascending key by keeping track of the
		 * last Node returned
		 * 
		 * @return K key for the next ascending key
		 */
		private K nextKey() {
			Iterator<DictionaryNode<K, V>> iter = bst.iterator();
			DictionaryNode<K, V> min = root;
			DictionaryNode<K, V> temp = root;
			if (last == null) {
				while (iter.hasNext()) {
					temp = iter.next();
					if (temp.compareTo(min) < 0) {
						min = temp;
					}
				}
				last = min;
				iterIndex++;
				return last.key;
			} else {
				min = null;
				while (iter.hasNext()) {
					temp = iter.next();
					if (temp.compareTo(last) > 0) {
						if (min == null || temp.compareTo(min) < 0) {
							min = temp;
						}

					}
				}
				last = min;
				iterIndex++;
				return last.key;
			}
		}
	}

	@Override
	public Iterator<V> values() {
		return new valIter();
	}

	private class valIter implements Iterator<V> {
		private int modificationCounter;
		private int iterIndex;
		private DictionaryNode<K, V> last;

		public valIter() {
			iterIndex = 0;
			modificationCounter = modCounter;
			last = null;
		}

		/**
		 * nextVal creates an iterator over the linkedlist structure without looking at
		 * the tree pointers and returns the next ascending key by keeping track of the
		 * last Node returned
		 * 
		 * @return V value for the next ascending key
		 */
		private V nextVal() {
			Iterator<DictionaryNode<K, V>> iter = bst.iterator();
			DictionaryNode<K, V> min = root;
			DictionaryNode<K, V> temp = root;
			if (last == null) {
				while (iter.hasNext()) {
					temp = iter.next();
					if (temp.compareTo(min) < 0) {
						min = temp;
					}
				}
				last = min;
				iterIndex++;
				return last.value;
			} else {
				min = null;
				while (iter.hasNext()) {
					temp = iter.next();
					if (temp.compareTo(last) > 0) {
						if (min == null || temp.compareTo(min) < 0) {
							min = temp;
						}
					}
				}
				last = min;
				iterIndex++;
				return last.value;
			}
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
			return nextVal();
		}

	}

}
