/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date: 09/04/2020
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first = null, last = null;
    private int counter = 0;

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> previous;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null) || (last == null);
    }

    // return the number of items on the deque
    public int size() {
        return counter;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("null argument is not valid");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        if (isEmpty()) last = first;
        else oldfirst.previous = first;
        counter++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("null argument is not valid");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        counter++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("the deque is empty");
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        else first.previous = null;
        counter--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("the deque is empty");
        Item item = last.item;
        last = last.previous;
        if (isEmpty()) first = null;
        else last.next = null;
        counter--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("there are no more items to return");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove function is unsupported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();

        StdOut.printf("Test 1:%n");
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
            deque.addLast(i);
        }
        for (int item : deque) StdOut.printf("%d, ", item);
        StdOut.printf("%nSize: " + deque.size());
        StdOut.printf("%nRemove: ");
        while (!deque.isEmpty()) StdOut.printf("F%d, ", deque.removeFirst());

        StdOut.printf("%n%nTest 2:%n");
        for (int i = 0; i < 10; i++) {
            deque.addLast(i);
            deque.addFirst(i);
        }
        for (int item : deque) StdOut.printf("%d, ", item);
        StdOut.printf("%nSize: " + deque.size());
        StdOut.printf("%nRemove: ");
        while (!deque.isEmpty()) StdOut.printf("L%d, ", deque.removeLast());

        StdOut.printf("%n%nTest 3:%n");
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
            deque.addLast(i);
        }
        for (int item : deque) StdOut.printf("%d, ", item);
        StdOut.printf("%nSize: " + deque.size());
        StdOut.printf("%nRemove: ");
        for (int i = 0; !deque.isEmpty(); i++) {
            if (i % 2 == 0) StdOut.printf("F%d, ", deque.removeFirst());
            else StdOut.printf("L%d, ", deque.removeLast());
        }

        StdOut.printf("%n%nTest 4:%n");
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
            deque.addLast(i);
        }
        for (int item : deque) StdOut.printf("%d, ", item);
        StdOut.printf("%nSize: " + deque.size());
        StdOut.printf("%nRemove: ");
        for (int i = 0; !deque.isEmpty(); i++) {
            if (i % 2 == 1) StdOut.printf("F%d, ", deque.removeFirst());
            else StdOut.printf("L%d, ", deque.removeLast());
        }

        StdOut.printf("%n%nTest 5:%n");
        StdOut.println("Size: " + deque.size());
        deque.addFirst(1);
        Iterator<Integer> iter = deque.iterator();
        StdOut.printf("%d%n", iter.next());
        iter.next();

        StdOut.printf("%n%nTest 6:%n");
        StdOut.println("Size: " + deque.size());
        deque.removeFirst();

        StdOut.printf("%n");
    }
}
