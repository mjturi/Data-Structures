/**
 * Program #2
 * Unordered Linked List Priority Queue
 * CS310
 * 3/10/20202
 * @author Matthew Turi cssc1284
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
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

	public UnorderedLinkedListPriorityQueue() {
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
		} else {
			newNode.next = head;
			head = newNode;
			size++;
			modCounter++;
			return true;
		}
	}

	@Override
	public E remove() {
		if (isEmpty())
			return null;
		Node<E> curr = head;
		Node<E> prev = head;
		Node<E> temp = head;
		while (curr.next != null) {
			if (temp.data.compareTo(curr.next.data) >= 0) {
				temp = curr.next;
				prev = curr;
				curr = curr.next;
			} else {
				curr = curr.next;
			}
		}
		if (temp == head) {
			head = temp.next;
			size--;
			modCounter++;
			return temp.data;
		}
		prev.next = temp.next;
		size--;
		modCounter++;
		return temp.data;
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
				if (curr == head) {
					head = curr.next;
					curr = curr.next;
				} else {
					prev.next = curr.next;
					curr = curr.next;
				}
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
		if (isEmpty())
			return null;
		Node<E> curr = head;
		Node<E> prev = head;
		Node<E> temp = head;
		while (curr.next != null) {
			if (temp.data.compareTo(curr.next.data) >= 0) {
				temp = curr.next;
				prev = curr;
				curr = curr.next;
			} else
				curr = curr.next;
		}
		return temp.data;
	}

	@Override
	public boolean contains(E obj) {
		Node<E> curr = head;
		if (size > 0) {
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
		if (size == 0) {
			return true;
		}
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