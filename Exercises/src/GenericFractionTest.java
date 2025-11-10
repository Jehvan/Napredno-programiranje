import java.awt.geom.GeneralPath;
import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
class GenericFraction<T extends Number,U extends Number> {
    T numerator;
    U denominator;
    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        this.numerator = numerator;
        if (!denominator.equals(0)) {
            this.denominator = denominator;
        } else {
            throw new ZeroDenominatorException("Denominator cannot be zero");
        }
    }


    GenericFraction<Double,Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {

        if (this.denominator.equals(gf.denominator)) {
            return new GenericFraction<Double, Double>((this.numerator.doubleValue() + gf.numerator.doubleValue()), this.denominator.doubleValue());
        } else {
            return new GenericFraction<Double,Double>((this.numerator.doubleValue() * gf.denominator.doubleValue() + gf.numerator.doubleValue() * this.denominator.doubleValue()), this.denominator.doubleValue() * gf.denominator.doubleValue());
        }

    }

    double toDouble() {
        return (numerator.doubleValue() / denominator.doubleValue());
    }

    @Override
    public String toString() {
        double numerator = this.numerator.doubleValue();
        double denominator = this.denominator.doubleValue();
        double bigger = 0;
        double smaller = 0;
        if (numerator > denominator) {
            bigger = numerator;
            smaller = denominator;
        } else {
            bigger = denominator;
            smaller = numerator;
        }

        for (double i = smaller; i > 0; i--){
            if (denominator % i == 0 && numerator % i == 0) {
                denominator /= i;
                numerator /= i;
            }
        }

        return (String.format("%.2f %s %.2f", numerator,"/",denominator));
    }

}

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException(String message) {
        super(message);
    }
}