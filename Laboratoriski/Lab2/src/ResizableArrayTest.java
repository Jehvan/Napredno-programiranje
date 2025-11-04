import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedList;

class ResizableArray<T> {
    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        this.elements = (T[]) new Object[100];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public void addElement(T element) {
        if (elements.length == size) {
            T[] new_elements = (T[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, new_elements, 0, elements.length);
            elements = new_elements;
            elements[size++] = element;
        } else {
            elements[size++] = element;
        }
    }

    public boolean removeElement(T element) {
        boolean removed = false;
        for (int i = 0; i < this.size; i++) {
            if (elements[i].equals(element)) {
                int numMoved = size - i - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, i + 1, elements, i, numMoved);
                }
                elements[--size] = null;
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        for (T t : this.elements) {
            if (t != null && t.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        Object[] copy = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            copy[i] = this.elements[i];
        }
        return copy;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int count() {
        return size;
    }

    public T elementAt(int idx) throws ArrayIndexOutOfBoundsException {
        if (idx < 0 || idx >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return elements[idx];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
//        int prov_size = 0;
        //System.arraycopy(src.elements, 0, dest.elements, dest.size, src.size);
        ResizableArray<? super T> copy = new ResizableArray<>();

        int size = src.size;
        for (int i = 0; i < size; i++) {
            if (src.elements[i] != null) {
                dest.addElement(src.elements[i]);

            }
        }


//        src.size = 0;
//        dest.size = 0;
//        for (int i = 0; i < src.elements.length; i++) {
//            if (src.elements[i] != null) {
//                src.size++;
//            }
//        }
//        for (int i = 0; i < dest.elements.length; i++) {
//            if (dest.elements[i] != null) {
//                dest.size++;
//            }
//        }

    }

}


class IntegerArray extends ResizableArray<Integer> {


    public IntegerArray() {
        super();
    }

    public double sum() {
        Integer total = 0;
        for (int i = 0; i < count(); i++) {
            total += elementAt(i);
        }
        return total;
    }

    public double mean() {
        Double total = 0.0;
        for (int i = 0; i < count(); i++) {
            total += elementAt(i);
        }
        return total / count();
    }

    public int countNonZero() {
        int count = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0) {
                count++;
            }
        }
        return count;
    }

    public IntegerArray distinct() {
        IntegerArray distinct = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            if (!distinct.contains(elementAt(i))) {
                distinct.addElement(elementAt(i));
            }
        }
        return distinct;
    }

    public IntegerArray increment(int offset) {
        IntegerArray inc = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            inc.addElement(elementAt(i) + offset);
        }
        return inc;
    }
}


public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
