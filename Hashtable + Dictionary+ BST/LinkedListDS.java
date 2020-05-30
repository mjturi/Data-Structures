package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDS<E> implements ListADT<E> {
	/////////////////////////////////////////////////////////////////
	class Node<E> {
		E data;
		Node<E> next;

		public Node(E obj) {
			data = obj;
			next = null;
		}
	}
	// END CLASS NODE ///////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	class ListIteratorHelper implements Iterator<E> {
		Node<E> index;

		public ListIteratorHelper() {
			index = head;
		}

		public boolean hasNext() {
			return index != null;
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			E tmp = index.data;
			index = index.next;
			return tmp;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
	// END CLASS LIST_ITERATOR_HELPER //////////////////////////////

	private Node<E> head, tail;
	private int currentSize;

	public LinkedListDS() {
		head = tail = null;
		currentSize = 0;
	}

	public void addFirst(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if (isEmpty())
			head = tail = newNode;
		else {
			newNode.next = head;
			head = newNode;
		}
		currentSize++;
	}

	public void addLast(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if (isEmpty())
			head = tail = newNode;
		else {
			tail.next = newNode;
			tail = newNode;
		}
		currentSize++;
	}

	public E removeFirst() {
		if (isEmpty())
			return null;
		E tmp = head.data;
		head = head.next;
		// if(head == null)
		// head = tail = null;
		currentSize--;
		return tmp;
	}

	public E removeLast() {
		if (isEmpty())
			return null;
		E tmp = tail.data;
		if (head == tail) // only one element in the list
			head = tail = null;
		else {
			Node<E> previous = null, current = head;
			while (current != tail) {
				previous = current;
				current = current.next;
			}
			previous.next = null;
			tail = previous;
		}

		currentSize--;
		return tmp;
	}

	public E peekFirst() {
		if (head == null)
			return null;
		return head.data;
	}

	public E peekLast() {
		if (tail == null)
			return null;
		return tail.data;
	}

	public boolean remove(E obj) {
		if (isEmpty())
			return false;
		Node<E> previous = null, current = head;
		while (current != null) {
			if (((Comparable<E>) current.data).compareTo(obj) == 0) {
				if (current == head)
					removeFirst();
				else if (current == tail)
					removeLast();
				else {
					previous.next = current.next;
					currentSize--;
				}
				return true;
			}
			previous = current;
			current = current.next;
		}
		return false;
	}

// not in the interface. Removes all instances of the key obj
	public boolean removeAllInstances(E obj) {
		Node<E> previous = null, current = head;
		boolean found = false;
		while (current != null) {
			if (((Comparable<E>) obj).compareTo(current.data) == 0) {
				if (previous == null) { // node to remove is head
					head = head.next;
					if (head == null)
						tail = null;
				} else if (current == tail) {
					previous.next = null;
					tail = previous;
				} else
					previous.next = current.next;
				found = true;
				currentSize--;
				current = current.next;
			} else {
				previous = current;
				current = current.next;
			}
		} // end while
		return found;
	}

	public void makeEmpty() {
		head = tail = null;
		currentSize = 0;
	}

	public boolean contains(E obj) {
		Node current = head;
		while (current != null) {
			if (((Comparable<E>) current.data).compareTo(obj) == 0)
				return true;
			current = current.next;
		}
		return false;
	}

	public E search(E obj) {
		Node current = head;
		while (current != null) {
			if (((Comparable<E>) current.data).compareTo(obj) == 0)
				return (E) current.data;
			current = current.next;
		}
		return null;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public boolean isFull() {
		return false;
	}

	public int size() {
		return currentSize;
	}

	public Iterator<E> iterator() {
		return new ListIteratorHelper();
	}
}