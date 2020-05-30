/** 
 * Program 3
 * Binary Heap Priority Queue
 * CS310
 * 4/5/20
 * @author Matthew Turi cssc1284
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private int maxSize, currentSize, modCounter, iterIndex;
	private Wrapper<E> heap[];

	/*
	 * Used the wrapper class from the Riggins course reader
	 */
	private class Wrapper<E> implements Comparable<Wrapper<E>> {
		long number;
		E data;

		public Wrapper(E d) {
			number = currentSize;
			data = d;
		}

		public int compareTo(Wrapper<E> o) {
			if (((Comparable<E>) data).compareTo(o.data) == 0)
				return (int) (number - o.number);
			return ((Comparable<E>) data).compareTo(o.data);
		}
	}

	/*
	 * Method to return the index of a node's parent
	 */
	private int parent(int index) {
		return (index - 1) / 2;
	}

	/*
	 * Method used to return the index of the next child with higher priority when
	 * given the parent index
	 */
	private int getNextChild(int index) {
		int child = 2 * index + 1;
		if (child > currentSize - 1)
			return -1;
		if ((child + 1 < currentSize) && (heap[child].compareTo(heap[child + 1]) > 0)) {
			return child + 1;
		}
		return child;
	}

	/*
	 * Trickle up used from Riggins course reader with some modifications
	 */
	private void trickleUp() {
		int newIndex = currentSize - 1;
		int parentIndex = (newIndex - 1) >> 1;
		Wrapper<E> temp = heap[newIndex];
		while (parentIndex >= 0 && temp.compareTo(heap[parentIndex]) < 0) {
			heap[newIndex] = heap[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex - 1) >> 1;
		}
		heap[newIndex] = temp;
	}

	/*
	 * Trickle down used from Riggins course reader with some modifications
	 */
	private void trickleDown() {
		int current = 0;
		int child = getNextChild(current);
		while (child != -1 && heap[current].compareTo(heap[child]) > 0) {
			Wrapper<E> temp = heap[current];
			heap[current] = heap[child];
			heap[child] = temp;
			current = child;
			child = getNextChild(current);
		}
	}

	@SuppressWarnings("unchecked")
	public BinaryHeapPriorityQueue() {
		heap = new Wrapper[DEFAULT_MAX_CAPACITY];
		maxSize = DEFAULT_MAX_CAPACITY;
		currentSize = 0;
	}

	public BinaryHeapPriorityQueue(int size) {
		heap = new Wrapper[size];
		maxSize = size;
		currentSize = 0;
	}

	@Override
	public boolean insert(E object) {
		if (isFull())
			return false;
		Wrapper<E> temp = new Wrapper<E>(object);
		heap[currentSize++] = temp;
		trickleUp();
		return true;
	}

	@Override
	public E remove() {
		if (isEmpty())
			return null;
		E temp = heap[0].data;
		heap[0] = heap[--currentSize];
		trickleDown();
		return temp;
	}

	@Override
	public boolean delete(E obj) {
		if (isEmpty())
			return false;
		E temp;
		boolean flag = false;
		for (int i = 0; i < currentSize; i++) {
			temp = heap[i].data;
			if (temp.compareTo(obj) == 0) {
				heap[i] = heap[--currentSize];
				if (heap[i].compareTo(heap[parent(i)]) > 0)
					trickleDown();
				else
					trickleUp();
				i = 0;
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public E peek() {
		if (isEmpty())
			return null;
		return heap[0].data;
	}

	@Override
	public boolean contains(E obj) {
		if (isEmpty())
			return false;
		int index = 0;
		E temp;
		for (int i = 0; i < currentSize; i++) {
			temp = heap[i].data;
			if (temp.compareTo(obj) == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public void clear() {
		currentSize = 0;
	}

	@Override
	public boolean isEmpty() {
		if (currentSize > 0)
			return false;
		return true;
	}

	@Override
	public boolean isFull() {
		return currentSize > maxSize;
	}

	@Override
	public Iterator<E> iterator() {
		return new iter();
	}

	class iter implements Iterator<E> {
		int modificationCounter;

		public iter() {
			iterIndex = 0;
			modificationCounter = modCounter;
		}

		@Override
		public boolean hasNext() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		@Override
		public E next() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			if (!hasNext())
				throw new NoSuchElementException();
			return heap[iterIndex++].data;
		}
	}
}
