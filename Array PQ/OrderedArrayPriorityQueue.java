/**
 * Program 1
 * Ordered Array Priority Queue using binary insertion and sort methods
 * CS310
 * 2/18/20
 * @author Matthew Turi cssc1284 
 */
package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private int maxSize, currentSize, iterIndex;
	private Comparable<E> storage[];
	private Iterator<E> iter;

	public OrderedArrayPriorityQueue() {
		storage = (E[]) new Comparable[DEFAULT_MAX_CAPACITY];
		iter = iterator();
		maxSize = DEFAULT_MAX_CAPACITY;
		currentSize = 0;
	}

	public OrderedArrayPriorityQueue(int size) {
		storage = (E[]) new Comparable[size];
		iter = iterator();
		maxSize = size;
		currentSize = 0;
	}

	@Override
	public boolean insert(E object) {
		if (isEmpty()) {
			storage[currentSize++] = object;
			return true;
		} else if (isFull()) {
			return false;
		}
		int temp = (binaryInsert(object));
		for (int i = currentSize; i > temp; --i) {
			if (i <= maxSize - 1)
				storage[i] = storage[i - 1];
		}
		storage[temp] = object;
		currentSize++;
		return true;

	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		iterIndex = 0;
		if (!isEmpty()) {
			E holder = (E) storage[0];
			for (int i = 0; i < currentSize; i++) {
				if (i < maxSize - 1) {
					storage[i] = storage[i + 1];
				}
			}
			--currentSize;
			return holder;
		}
		return null;
	}

	@Override
	public boolean delete(E obj) {
		// TODO Auto-generated method stub
		boolean found = false;
		if (isEmpty())
			return found;
		iterIndex = 0;
		while (iter.hasNext()) {
			int temp = binarySearch(obj);
			if (temp != -1) {
				found = true;
				for (int j = temp; j < currentSize - 1; j++) {
					if (j < maxSize - 1) {
						storage[j] = storage[j + 1];
					}
				}
				currentSize--;
			} else
				break;
		}
		return found;
	}

	@Override
	public E peek() {
		// TODO Auto-generated method stub
		if (isEmpty())
			return null;
		return (E) storage[0];

	}

	@Override
	public boolean contains(E obj) {
		// TODO Auto-generated method stub
		if (isEmpty())
			return false;
		if (binarySearch(obj) != -1) {
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return currentSize;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		currentSize = 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (currentSize == 0)
			return true;
		return false;
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		if (currentSize == maxSize)
			return true;
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		iterIndex = 0;
		Iterator<E> iter = new Iterator<E>() {

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return iterIndex < currentSize;
			}

			@Override
			public E next() {
				// TODO Auto-generated method stub
				if (!hasNext())
					throw new NoSuchElementException();
				return (E) storage[iterIndex++];
			}

		};
		return iter;
	}

	/**
	 * Binary Insertion method, determines the index where the new object should
	 * go in the ordered array
	 * 
	 * @param obj
	 * @return integer (index number)
	 */
	private int binaryInsert(E obj) {
		int low = 0;
		for (int i = 0; i < currentSize; i++) {
			low = 0;
			int high = currentSize;
			while (low < high) {
				int mid = low + (high - low) / 2;
				if (obj.compareTo((E) storage[mid]) < 0) {
					high = mid;
				} else
					low = mid + 1;
			}

		}
		return low;
	}

	/**
	 * Binary Search method, determines if the object is in the array
	 * 
	 * @param obj
	 * @return integer (index number if in array, -1 if not)
	 */
	private int binarySearch(E obj) {
		int low = 0;
		for (int i = 0; i < currentSize; i++) {
			low = 0;
			int high = currentSize;
			while (low < high) {
				int mid = low + (high - low) / 2;
				if (obj.compareTo((E) storage[mid]) == 0) {
					return mid;
				} else if (obj.compareTo((E) storage[mid]) < 0) {
					high = mid;
				} else
					low = mid + 1;
			}
		}
		return -1;
	}

}
