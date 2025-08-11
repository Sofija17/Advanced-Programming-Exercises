package First_Midterm;
//
//Да се дефинира класа ShapesApplication во која се чуваат податоци за повеќе прозорци
// на кои се исцртуваат геометриски слики во форма на квадрат.
//
//За класата да се дефинира:
//
//ShapesApplication() - конструктор
//int readCanvases (InputStream inputStream) - метод којшто од влезен поток на податоци ќе прочита
// информации за повеќе прозорци на кои се исцртуваат квадрати.
// Во секој ред од потокот е дадена информација за еден прозорец во формат:
// canvas_id size_1 size_2 size_3 …. size_n, каде што canvas_id е ИД-то на прозорецот,
// а после него следуваат големините на страните на квадратите што се исцртуваат во прозорецот.
// Методот треба да врати цел број што означува колку квадрати за сите прозорци се успешно прочитани.
//void printLargestCanvasTo (OutputStream outputStream) - метод којшто на излезен поток ќе го испечати
// прозорецот чии квадрати имаат најголем периметар. Печатењето да се изврши во форматот
// canvas_id squares_count total_squares_perimeter.

//364fbe94 24 30 22 33 32 30 37 18 29 27 33 21 27 26

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Canvas implements Comparable<Canvas> {
    private String canvas_id;
    private List<Integer> squares;

    public Canvas(String line) {
        this.squares = new ArrayList<>();
        String[] parts = line.split("\\s+");
        this.canvas_id = parts[0];

//        for (int i=1; i< parts.length; i++){
//            this.squares.add(Integer.parseInt(parts[i]));
//        }

        IntStream.range(1, parts.length)
                .mapToObj(s -> Integer.parseInt(parts[s]))
                .forEach(m -> this.squares.add(m));

    }

    public String getCanvas_id() {
        return canvas_id;
    }

    public List<Integer> getSquares() {
        return squares;
    }

    public int squareCount() {
        return squares.size();
    }

    public int perimeter() {
//        int sum=0;
//        for (Integer size: squares){
//            sum += size;
//        }
//
//        return sum*4;
        return squares.stream()
                .mapToInt(s -> s.intValue())
                .sum() * 4;

    }

    @Override
    public int compareTo(Canvas other) {
        return Integer.compare(this.perimeter(), other.perimeter());
    }
}

class ShapesApplication {

    private List<Canvas> canvases;

    public ShapesApplication() {
        this.canvases = new ArrayList<>();
    }


    public int readCanvases(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        this.canvases = br.lines()
                .map(line -> new Canvas(line))
                .collect(Collectors.toList());

//        int count=0;
//        for (Canvas canvas : canvases){
//            count += canvas.squareCount();
//        }

        int count2 = canvases.stream()
                .mapToInt(canvas -> canvas.squareCount())
                .sum();

        return count2;
    }

    public void printLargestCanvasTo(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        canvases.stream()
                .max(Comparator.naturalOrder())
                .ifPresent(winner ->
                        pw.println(winner.getCanvas_id() + " " + winner.squareCount() + " " + winner.perimeter()));

//        int max_perimeter = this.canvases.stream()
//                .mapToInt(c -> c.perimeter())
//                .max()
//                .orElse(0);
//
//        Canvas winner = null;
//        for (Canvas c : canvases){
//            if(c.perimeter() == max_perimeter){
//                winner = c;
//            }
//        }
//
//        pw.println(winner.getCanvas_id() + " " + winner.squareCount() + " " + winner.perimeter());
        pw.flush();
        pw.close();

    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
