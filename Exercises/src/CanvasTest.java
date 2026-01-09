import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CanvasTest {

    public static void main(String[] args) throws InvalidIDException {
        Canvas3 canvas = new Canvas3();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}

abstract class Shape2 {
    double perimeter;
    double area;

    abstract void calculatePerimeter();

    abstract void calculateArea();

    abstract void scale(double coef);
}

class Circle2 extends Shape2 {
    double radius;

    public Circle2(double radius) {
        this.radius = radius;
        this.calculateArea();
        this.calculatePerimeter();
    }


    @Override
    void calculatePerimeter() {
        this.perimeter = 2 * this.radius * Math.PI;
    }

    @Override
    void calculateArea() {
        this.area = Math.pow(this.radius, 2) * Math.PI;
    }

    @Override
    void scale(double coef) {
        this.radius *= coef;
    }
}

class Square extends Shape2 {
    double side;

    public Square(double side) {
        this.side = side;
        this.calculateArea();
        this.calculatePerimeter();
    }

    @Override
    void calculatePerimeter() {
        this.perimeter = 4 * side;
    }

    @Override
    void calculateArea() {
        this.area = side * side;
    }

    @Override
    void scale(double coef) {
        this.side *= coef;
    }
}

class Rectangle2 extends Shape2 {
    double width;
    double height;

    public Rectangle2(double width, double height) {
        this.width = width;
        this.height = height;
        this.calculateArea();
        this.calculatePerimeter();
    }

    @Override
    void calculatePerimeter() {
        this.perimeter = 2 * width + 2 * height;
    }

    @Override
    void calculateArea() {
        this.area = width * height;
    }

    @Override
    void scale(double coef) {
        this.width *= coef;
        this.height *= coef;
    }
}

class Canvas3 {
    Map<String, List<Shape2>> map = new HashMap<>();
    int count = 0;
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;
    double avg = 0;
    double sum = 0;

    public Canvas3() {
    }

    void readShapes(InputStream is) throws InvalidIDException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.lines().forEach(
                line -> {
                    String[] tokens = line.split("\\s+");
                    String form = tokens[0];
                    String id = tokens[1];
                    if (!id.chars().allMatch(Character::isLetterOrDigit)) try {
                        throw new InvalidIDException("Invalid ID.");
                    } catch (InvalidIDException e) {
                    }
                    ;
                    if (form.equals("3")) {
                        double width = Double.parseDouble(tokens[2]);
                        double height = Double.parseDouble(tokens[3]);
                        if (width == 0 || height == 0) try {
                            throw new InvalidDimensionException("Dimension 0 is not allowed!");
                        } catch (InvalidDimensionException e) {
                            throw new RuntimeException(e);
                        }
                        Rectangle2 s = new Rectangle2(width, height);
                        map.computeIfAbsent(id, k -> new ArrayList<>()).forEach(
                                k -> {
                                    if (s.area > k.area) {
                                        List<Shape2> tmp;
                                        tmp = map.get(id).subList(0,map.get(id).indexOf(k));
                                        tmp.add(s);
                                        tmp.addAll(map.get(id).indexOf(k),map.get(id));
                                        map.remove(id,map.get(id));
                                        map.put(id, tmp);
                                    }
                                }
                        );
                    } else {
                        double side = Double.parseDouble(tokens[2]);
                        if (side == 0) try {
                            throw new InvalidDimensionException("Dimension 0 is not allowed!");
                        } catch (InvalidDimensionException e) {
                            throw new RuntimeException(e);
                        }
                        if (form.equals("1")) {
                            Circle2 s = new Circle2(side);
                            map.computeIfAbsent(id, k -> new ArrayList<>()).stream().forEach(
                                    k -> {
                                        if (s.area > k.area) {
                                            List<Shape2> tmp;
                                            tmp = map.get(id).subList(0,map.get(id).indexOf(k));
                                            tmp.add(s);
                                            tmp.addAll(map.get(id).indexOf(k),map.get(id));
                                            map.remove(id,map.get(id));
                                            map.put(id, tmp);
                                        }
                                    }
                            );
                        } else {
                            Square s = new Square(side);
                            map.computeIfAbsent(id, k -> new ArrayList<>()).forEach(
                                    k -> {
                                        if (s.area > k.area) {
                                            List<Shape2> tmp;
                                            tmp = map.get(id).subList(0,map.get(id).indexOf(k));
                                            tmp.add(s);
                                            tmp.addAll(map.get(id).indexOf(k),map.get(id));
                                            map.remove(id,map.get(id));
                                            map.put(id, tmp);
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }

    void scaleShapes(String userID, double coef) {
        map.get(userID).forEach(s -> s.scale(coef));
    }

    void printAllShapes(OutputStream os) {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        for (String id : map.keySet()){
            map.get(id).forEach(s -> {
                try {
                    bw.write(s.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    void printByUserId(OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        // Create a list of map entries to sort
        List<Map.Entry<String, List<Shape2>>> entries = new ArrayList<>(map.entrySet());

        // Sort users: by number of shapes descending, then by total area descending
        entries.sort((a, b) -> {
            int countCmp = Integer.compare(b.getValue().size(), a.getValue().size());
            if (countCmp != 0) return countCmp;
            double areaA = a.getValue().stream().mapToDouble(shape -> shape.area).sum();
            double areaB = b.getValue().stream().mapToDouble(shape -> shape.area).sum();
            return Double.compare(areaB, areaA); // Descending
        });

        for (Map.Entry<String, List<Shape2>> entry : entries) {
            String userID = entry.getKey();
            List<Shape2> shapes = new ArrayList<>(entry.getValue());
            // For each user, sort shapes by perimeter DESCENDING (using Collections.sort, not streams)
            shapes.sort((s1, s2) -> Double.compare(s2.perimeter, s1.perimeter));

            pw.println("User: " + userID);
            for (Shape2 s : shapes) {
                pw.printf("Area: %.2f, Perimeter: %.2f%n", s.area, s.perimeter);
            }
        }
        pw.flush();
    }

    void statistics(OutputStream os){
        for (String k : map.keySet()){
            map.get(k).forEach(s -> {
                count++;
                if(s.area < min) min = s.area;
                if(s.area > max) max = s.area;
                sum += s.area;
            });
        }
        avg = sum/count;
        PrintWriter pw = new PrintWriter(os);
        pw.printf("count: %d",count);
        pw.printf("sum: %.2f",sum);
        pw.printf("min: %.2f",min);
        pw.printf("average: %.2f",avg);
        pw.printf("max: %.2f",max);
    }



}


class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super(message);
    }
}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}
