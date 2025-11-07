import java.util.Scanner;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T extends Comparable<T>> {
    T min;
    T max;
    int count = 0;
    public MinMax() {}

    void update (T element) {

        if (this.min == null && this.max == null) {
            this.min = element;
            this.max = element;
        } else {
            if (!element.equals(this.min) && !element.equals(this.max)) {
                this.count++;
            }
            if (element.compareTo(this.min) < 0) {
                this.min = element;
            }
            if (element.compareTo(this.max) > 0) {
                this.max = element;
            }
        }
    }

    public T getMin() {
        return this.min;
    }

    public T getMax() {
        return this.max;
    }

    @Override
    public String toString() {
        return this.min + " " + this.max + " " + this.count + "\n";
    }
}