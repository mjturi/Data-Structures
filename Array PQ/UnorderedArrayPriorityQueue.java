/**
 * Program 1
 * Unordered Array Priority Queue 
 * CS310
 * 2/18/20
 * @author Matthew Turi cssc1284 
 */
package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private Comparable<E> storage[];
	private Iterator<E> iter;
	private int currentSize, maxSize, iterIndex;

	@SuppressWarnings("unchecked")
	public UnorderedArrayPriorityQueue() {
		storage = (E[]) new Comparable[DEFAULT_MAX_CAPACITY];
		iter = iterator();
		maxSize = DEFAULT_MAX_CAPACITY;
		currentSize = 0;
	}

	@SuppressWarnings("unchecked")
	public UnorderedArrayPriorityQueue(int size) {
		storage = (E[]) new Comparable[size];
		iter = iterator();
		maxSize = size;
		currentSize = 0;
	}

	@Override
	public boolean insert(E object) {
		if (!isFull()) {
			storage[currentSize++] = object;
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove() {
		if (isEmpty())
			return null;
		iterIndex = 0;
		int temp = 0;
		E holder;
		while (iter.hasNext()) {
			if (storage[temp].compareTo(iter.next()) > 0) {
				temp = iterIndex - 1;
			}
		}
		holder = (E) storage[temp];
		for (int i = temp; i < currentSize; i++) {
			if (i < maxSize - 1)
				storage[i] = storage[i + 1];
		}
		currentSize--;
		return holder;
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(E obj) {
		// TODO Auto-generated method stub
		boolean found = false;
		iterIndex = 0;
		while (iter.hasNext()) {
			if (obj.compareTo(iter.next()) == 0) {
				for (int i = --iterIndex; i < currentSize - 1; i++) {
					storage[i] = storage[i + 1];
				}
				found = true;
				currentSize--;
			}
		}
		return found;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E peek() {
		// TODO Auto-generated method stub
		if (isEmpty())
			return null;
		iterIndex = 0;
		int temp = 0;
		E holder;
		while (iter.hasNext()) {
			if (storage[temp].compareTo(iter.next()) > 0) {
				temp = iterIndex - 1;
			}
		}
		holder = (E) storage[temp];
		return holder;
	}

	@Override
	public boolean contains(E obj) {
		// TODO Auto-generated method stub
		iterIndex = 0;
		while (iter.hasNext()) {
			if (obj.compareTo(iter.next()) == 0) {
				return true;
			}
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
		iterIndex = 0;
		Iterator<E> iter = new Iterator<E>() {

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return iterIndex < currentSize;
			}

			@SuppressWarnings("unchecked")
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

}
