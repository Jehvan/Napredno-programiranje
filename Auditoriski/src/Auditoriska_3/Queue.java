package Auditoriska_3;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Queue<E> {

    private ArrayList<Item> items;

    public Queue() {
        items = new ArrayList<>();
    }

    public void add(Item item) {
        items.add(item);
    }

    @Override
    public String toString() {
        return "Queue {" + "items=" + items + '}';
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<String>();
        queue.add(new Item<>("A",1));
        queue.add(new Item<>("B",2));
        queue.add(new Item<>("C",3));
        queue.add(new Item<>("D",1));
        queue.add(new Item<>("E",12));
        System.out.println(queue);
    }
}
