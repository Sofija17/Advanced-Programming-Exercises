package First_Midterm;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String canvas_id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", canvas_id, maxArea));
    }
}

abstract class Shape {

    abstract double area();

}

class Circle extends Shape {
    private double radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Square extends Shape {
    private double side;

    public Square(int side) {
        this.side = side;
    }

    @Override
    double area() {
        return side * side;
    }
}


class Canvas2 implements Comparable<Canvas2> {
    private String id;
    private List<Shape> shapes;
    private double maxArea;


    public Canvas2(String id, List<Shape> shapes, double area) throws IrregularCanvasException {
        this.id = id;
        this.shapes = shapes;
        this.maxArea = area;

        for (Shape s : shapes) {
            if (s.area() > maxArea) {
                throw new IrregularCanvasException(this.id, this.maxArea);
            }
        }

    }

    public static Canvas2 createCanvas(String line, double maxArea) {
        String[] parts = line.split(" ");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();

        for (int i = 1; i < parts.length; i += 2) {
            if (parts[i].equals("C")) {

                shapes.add(new Circle(Integer.parseInt(parts[i + 1])));
            } else {
                shapes.add(new Square(Integer.parseInt(parts[i + 1])));
            }
        }
        try {
            return new Canvas2(id, shapes, maxArea);
        } catch (IrregularCanvasException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double sumOfAreas() {
        return shapes.stream()
                .mapToDouble(Shape::area)
                .sum();
    }

    @Override
    public int compareTo(Canvas2 other) {
        return Double.compare(this.sumOfAreas(), other.sumOfAreas());
    }

    //    ID total_shapes total_circles total_squares min_area max_area average_area.
    @Override
    public String toString() {
        int numCircles = (int) shapes.stream().filter(s -> s instanceof Circle).count();
        int numSquares = (int) shapes.stream().filter(s -> s instanceof Square).count();

        DoubleSummaryStatistics stats = shapes.stream()
                .mapToDouble(s -> s.area())
                .summaryStatistics();

        return String.format(
                "%s %d %d %d %.2f %.2f %.2f",
                this.id, this.shapes.size(), numCircles, numSquares,
                stats.getMin(), stats.getMax(), stats.getAverage());
    }
}

class ShapesApplication2 {
    //chuva canvases
    private double maxArea;
    private List<Canvas2> canvases;

    public ShapesApplication2(double maxArea) {
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }

    // canvas_id type_1 size_1 type_2 size_2 type_3 size_3 …type_n size_n
    //0cc31e47 C 27 C 13 C 29 C 15 C 22
    void readCanvases(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        this.canvases = br.lines()
                .map((String line) -> Canvas2.createCanvas(line, maxArea))
                .filter(obj -> Objects.nonNull(obj)) // важно
                .collect(Collectors.toList());

    }

    //sortirani vo opagacki redosled spored suma na ploshtini
    void printCanvases(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        canvases.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(c -> pw.println(c));

        pw.flush();
    }

}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication2 shapesApplication = new ShapesApplication2(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
