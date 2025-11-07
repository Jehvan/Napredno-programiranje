import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

class Canvas {
    String canvasId;
    List<Integer> squares;

    public Canvas(String canvasId) {
        this.canvasId = canvasId;
        this.squares = new ArrayList<>();
    }

    public int totalPerimeter() {
        int sum = 0;
        for (int side : squares) {
            sum += 4 * side;
        }
        return sum;
    }
}

class ShapesApplication {
    private List<Canvas> canvases = new ArrayList<>();

    public ShapesApplication() {}

    public int readCanvases(InputStream in) {
        Scanner sc = new Scanner(in);
        int totalSquares = 0;
        while(sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if(line.isEmpty()) break;
            String[] split = line.split("\\s+");
            if (split.length < 2) {break;}
            Canvas canvas = new Canvas(split[0]);
            for  (int i = 1; i < split.length; i++) {
                canvas.squares.add(Integer.parseInt(split[i]));
                totalSquares += 1;
            }
            canvases.add(canvas);
        }
        return totalSquares;
    }
    public void printLargestCanvasTo(PrintStream out) {
        Canvas largest = null;
        int maxPerimeter = 0;
        for (Canvas canvas : canvases) {
            if (canvas.totalPerimeter() > maxPerimeter) {
                maxPerimeter = canvas.totalPerimeter();
                largest = canvas;
            }
        }
        if (largest != null) {
            PrintStream printStream = new PrintStream(System.out);
            printStream.printf("%s %d %d\n", largest.canvasId, largest.squares.size(), largest.totalPerimeter());
        }
    }
}