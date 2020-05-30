/**
 * Program #2
 * Ordered Linked List Priority Queue
 * CS310
 * 3/10/2020
 * @author Matthew Turi cssc1284
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private Node<E> head;
	private int size, modCounter;

	private class Node<E> {
		E data;
		Node<E> next;

		public Node(E object) {
			data = object;
			next = null;
		}
	}

	public OrderedLinkedListPriorityQueue() {
		modCounter = 0;
		head = null;
		size = 0;
	}

	@Override
	public boolean insert(E object) {
		Node<E> newNode = new Node<E>(object);
		if (isEmpty()) {
			newNode.next = head;
			head = newNode;
			size++;
			modCounter++;
			return true;
		} else if (size == 1) {
			if (head.data.compareTo(newNode.data) > 0) {
				Node<E> temp = head;
				head = newNode;
				newNode.next = temp;
				modCounter++;
				size++;
			} else {
				head.next = newNode;
				modCounter++;
				size++;
			}
		} else {
			Node<E> curr = head;
			Node<E> prev = head;
			while (curr.next != null) {
				if (curr.data.compareTo(newNode.data) > 0) {
					break;
				} else {
					prev = curr;
					curr = curr.next;
				}
			}
			if (curr == head) {
				head = newNode;
				newNode.next = curr;
			} else if (curr.data.compareTo(newNode.data) <= 0) {
				curr.next = newNode;
			} else {
				prev.next = newNode;
				newNode.next = curr;
			}
			size++;
			modCounter++;
		}
		return true;
	}

	@Override
	public E remove() {
		if (!isEmpty()) {
			Node<E> newNode = head;
			head = newNode.next;
			size--;
			modCounter++;
			return newNode.data;
		}
		return null;
	}

	@Override
	public boolean delete(E obj) {
		if (isEmpty())
			return false;
		Node<E> curr = head;
		Node<E> prev = head;
		boolean found = false;
		while (curr.next != null) {
			if (curr.data.compareTo(obj) == 0) {
				found = true;
				prev.next = curr.next;
				curr = curr.next;
				size--;
				modCounter++;
			} else {
				if (curr != head)
					prev = prev.next;
				curr = curr.next;
			}
		}
		return found;
	}

	@Override
	public E peek() {
		if (!isEmpty()) {
			Node<E> newNode = head;
			return newNode.data;
		}
		return null;
	}

	@Override
	public boolean contains(E obj) {
		Node<E> curr = head;
		if (!isEmpty()) {
			while (curr.next != null) {
				if (curr.data.compareTo(obj) == 0) {
					return true;
				} else {
					curr = curr.next;
				}
			}
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		head = null;
		size = 0;
		modCounter++;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0)
			return true;
		return false;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new iter();
	}

	class iter implements Iterator<E> {
		Node<E> nodePtr;
		int modificationCounter;

		public iter() {
			nodePtr = head;
			modificationCounter = modCounter;
		}

		@Override
		public boolean hasNext() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			return nodePtr != null;
		}

		@Override
		public E next() {
			if (modificationCounter != modCounter)
				throw new ConcurrentModificationException();
			if (!hasNext())
				throw new NoSuchElementException();
			E tmp = nodePtr.data;
			nodePtr = nodePtr.next;
			return tmp;
		}
	}
}
