/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date: 10/04/2020
 *  
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] stack;
    private int counter = 0;

    // construct an empty randomized queue
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        stack = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return counter == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return counter;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("null argument is not valid");
        if (counter == stack.length) resize(2 * stack.length);
        stack[counter++] = item;
    }

    // resize stack array
    private void resize(int capacity) {
        @SuppressWarnings("unchecked")
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < counter; i++) copy[i] = stack[i];
        stack = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("the queue is empty");
        int rndi = StdRandom.uniform(counter);
        swap(rndi, --counter);
        Item item = stack[counter];
        stack[counter] = null;
        if (counter > 0 && counter == stack.length / 4) resize(stack.length / 2);
        return item;
    }

    // swap element i with j element of array
    private void swap(int i, int j) {
        Item item = stack[i];
        stack[i] = stack[j];
        stack[j] = item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("the queue is empty");
        int rndi = StdRandom.uniform(counter);
        return stack[rndi];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomListIterator();
    }

    private class RandomListIterator implements Iterator<Item> {
        private int[] permutations = StdRandom.permutation(counter);
        private int current = 0;

        public boolean hasNext() {
            return current != counter;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("there are no more items to return");
            int rndi = permutations[current];
            current++;
            return stack[rndi];
        }

        public void remove() {
            throw new UnsupportedOperationException("remove function is unsupported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rndQueue = new RandomizedQueue<Integer>();
        int n;
        if (args.length >= 1) n = Integer.parseInt(args[0]);
        else n = 10;
        for (int i = 0; i < n; i++) rndQueue.enqueue(i + 1);
        for (int i = 0; i < n; i++) StdOut.printf("%d, ", rndQueue.dequeue());
        StdOut.println();
        for (int i = 0; i < n; i++) rndQueue.enqueue(i + 1);
        for (int item : rndQueue) StdOut.printf("%d, ", item);
        StdOut.println();
        StdOut.println("Size: " + rndQueue.size());
    }

}
