import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}

class Canvas {
    String canvas_id;
    List<String> shapes;
    double max_area = Integer.MIN_VALUE;
    double min_area = Integer.MAX_VALUE;
    public Canvas(String canvas_id) {
        this.canvas_id = canvas_id;
        this.shapes = new ArrayList<>();
    }


    public double getCanvasArea() {
        double total = 0;
        for (int i = 0; i < shapes.size(); i += 2) {
            if (shapes.get(i).equals("S")) {
                total += Double.parseDouble(shapes.get(i + 1)) * Double.parseDouble(shapes.get(i + 1));
            } else if (shapes.get(i).equals("C")) {
                total += Double.parseDouble((shapes.get(i + 1))) * Double.parseDouble(shapes.get(i + 1)) * Math.PI;
            }
        }
        return total;
    }

    public void setCanvas_id(String canvas_id) {
        this.canvas_id = canvas_id;
    }
}

class ShapesApplication  {
    double maxArea;
    List<Canvas> canvases = new ArrayList<>();

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    void readCanvases(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) break;
            String[] split = line.split("\\s+");
            String id = split[0];
            Canvas canvas = new Canvas(id);
            double min_area = Integer.MAX_VALUE;
            double max_area = Integer.MIN_VALUE;
            try {
                for (int i = 1; i < split.length; i+=2) {
                    double area = 0;
                    if (split[i].equals("S")) {
                        area = Double.parseDouble(split[i + 1]) * Double.parseDouble(split[i + 1]);
                    } else if (split[i].equals("C")) {
                        area = Double.parseDouble((split[i + 1])) *  Double.parseDouble(split[i + 1]) * Math.PI;
                    }
                    canvas.shapes.add(split[i]);
                    canvas.shapes.add(split[i + 1]);
                    if (area > max_area) {
                        max_area = area;
                        canvas.max_area = area;
                    }
                    if (area < min_area) {
                        min_area = area;
                        canvas.min_area = area;
                    }
                    if (area > maxArea) {
                        throw new InvalidCanvasException(id, maxArea);
                    }
                }
                canvas.max_area = max_area;
                canvas.min_area = min_area;
                canvases.add(canvas);
            } catch (InvalidCanvasException e) {
                System.out.println(e.getMessage());
            }

        }
    }


    public int countSquares(Canvas canvas) {
        int total = 0;
        for (int i = 0; i < canvas.shapes.size(); i += 2) {
            if (canvas.shapes.get(i).equals("S")) {
                total++;
            }
        }
        return total;
    }

    public int countCircles(Canvas canvas) {
        int total = 0;
        for (int i = 0; i < canvas.shapes.size(); i += 2) {
            if (canvas.shapes.get(i).equals("C")) {
                total++;
            }
        }
        return total;
    }

    public void printCanvases(OutputStream out) {
        List<Canvas> sorted = this.canvases.stream().sorted(Comparator.comparing(Canvas::getCanvasArea).reversed()).collect(Collectors.toList());
        PrintStream ps = new PrintStream(out);
        for (Canvas canvas : sorted) {
            ps.printf("%s %d %d %d %.2f %.2f %.2f",canvas.canvas_id, (this.countCircles(canvas) + this.countSquares(canvas)), this.countCircles(canvas), this.countSquares(canvas), canvas.min_area, canvas.max_area, (canvas.getCanvasArea()/canvas.shapes.size()*2));
            ps.println();
        }

    }

}

class InvalidCanvasException extends Exception {
    String canvasId;
    double maxArea;
    public InvalidCanvasException(String canvasId, double maxArea) {
        this.canvasId = canvasId;
        this.maxArea = maxArea;
    }
    @Override
    public String getMessage() {
        return String.format("%s %s %s %.2f","Canvas", canvasId, "has a shape with area larger than", maxArea);

    }
}