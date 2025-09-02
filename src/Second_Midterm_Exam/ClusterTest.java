package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 2
 */

interface IPoints {
    long getId();

    double distance(float x2, float y2);

    float getX();
    float getY();
}

class Cluster<T extends IPoints> {
    private List<T> points;

    public Cluster() {
        this.points = new ArrayList<>();
    }

    void addItem(T item) {
        this.points.add(item);
    }

    void near(long id, int top) {

        IPoints point = points.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .get();

        List<T> nearestPoints = points.stream()
                .filter(p -> p.getId() != id)
                .sorted(Comparator.comparingDouble(p -> p.distance(point.getX(), point.getY())))
                .limit(top)
                .collect(Collectors.toList());

        for (int i=0; i<nearestPoints.size(); i++){
            IPoints p = nearestPoints.get(i);
            double d = p.distance(point.getX(), point.getY());
            System.out.printf("%d. %d -> %.3f\n", i + 1, p.getId(), d);
        }

    }
}

class Point2D implements IPoints {
    private long id;
    private float x;
    private float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public double distance(float x2, float y2) {
        //sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}
        double a = Math.pow(this.x - x2, 2);
        double b = Math.pow(this.y - y2, 2);

        return Math.sqrt(a + b);
    }

    @Override
    public String toString() {
        return String.format("%f -> ", id);
    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here
