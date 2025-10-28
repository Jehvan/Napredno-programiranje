package Auditoriska_3;

import java.util.ArrayList;

public class Box<E> {

    ArrayList<E> lista = new ArrayList<>();

    public void add(E e) {
        lista.add(e);
    }

    public boolean isEmpty() {
        return !lista.isEmpty();
    }

    public void drawItem() {
        System.out.println(lista.removeFirst());;
    }


    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>();
        integerBox.add(1);
        integerBox.add(30000);
        while(integerBox.isEmpty()){
            integerBox.drawItem();
        }

        Box<String> stringBox = new Box<String>();
        stringBox.add("Hello");
        while(stringBox.isEmpty())
            stringBox.drawItem();

        Box<Person> personBox = new Box<Person>();
        personBox.add(new Person("John","Doe"));
        personBox.add(new Person("John","Smith"));
        while(personBox.isEmpty())
            personBox.drawItem();
    }
}
