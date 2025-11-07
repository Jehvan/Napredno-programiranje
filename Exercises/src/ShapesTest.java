import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}


public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas2 canvas = new Canvas2();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

abstract class Shape implements Stackable, Scalable {
    String id;
    Color color;

    Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    abstract String getType();
}

class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    String getType() {
        return "C";
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * this.radius * this.radius);
    }
}

class Rectangle extends Shape {
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public String getType() {
        return "R";
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return (this.width * this.height);

    }
}

class Canvas2 {
    List<Shape> shapes;

    public Canvas2() {
        List<Shape> shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        Shape shape = new Circle(id, color, radius);
        insert(shape);
    }

    public void add(String id, Color color, float width, float height) {
        Shape shape = new Rectangle(id, color, width, height);
        insert(shape);
    }

    public void insert(Shape shape) {
        if (this.shapes == null) {
            this.shapes = new ArrayList<>();
            shapes.add(shape);
        } else {
            float newWeight = shape.weight();
            int insertIndex = shapes.size();
            for (int i = 0; i < shapes.size(); i++) {
                if (newWeight > shapes.get(i).weight()) {
                    insertIndex = i;
                    break;
                }
            }
            shapes.add(insertIndex, shape);
        }

    }

    void scale(String id, float scaleFactor) {
        for (Shape shape : shapes) {
            if (shape.id.equals(id)) {
                Shape tmp = shape;
                shape.scale(scaleFactor);
                shapes.remove(tmp);
                insert(shape);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(String.format("%s: %5s %-10s %10.2f%n",
                    shape.getType(),
                    shape.id,
                    shape.color.toString(),
                    shape.weight()));
        }
        return sb.toString();
    }
}