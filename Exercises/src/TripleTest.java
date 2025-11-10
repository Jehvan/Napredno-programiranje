import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;


public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


class Triple<Number extends Comparable<Number>> {
    Number element1, element2, element3;
    List<Number> list;

    public Triple(Number element1, Number element2, Number element3) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
        list = new ArrayList<>();
        list.add(element1);
        list.add(element2);
        list.add(element3);
    }

    double max() {
        Number max = list.stream().max(Comparator.comparing(s -> Double.parseDouble(s.toString()))).stream().collect(Collectors.toList()).get(0);
        return Double.parseDouble(max.toString());
    }

    double avarage() {
        return (Double.parseDouble(element1.toString()) + Double.parseDouble(element2.toString()) + Double.parseDouble(element3.toString())) / 3;
    }

    void sort() {
        list.sort(Comparator.comparing(s -> Double.parseDouble(s.toString())));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Number number : list) {
            double tmp = Double.parseDouble(number.toString());
            System.out.printf("%.2f %s",tmp,"");
        }
        return "";
    }
}