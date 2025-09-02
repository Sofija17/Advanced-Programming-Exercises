package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}

class Student{
    private String index;
    private List<Integer> labPoints;

    private static final int CURRENT_YEAR = 20;

    public Student(String index, List<Integer> labPoints) {
        this.index = index;
        this.labPoints = labPoints;
    }

    double calculateAvgPoints(){
        double sum = labPoints.stream().mapToInt(Integer::valueOf).sum();

        return sum/10.0;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getLabPoints() {
        return labPoints;
    }

    public boolean getsSignature(){
        return labPoints.size()>=8;
    }

    public int getYear(){
        int idxYear = Integer.parseInt(index.substring(0,2));
        return CURRENT_YEAR-idxYear;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, getsSignature() ? "YES" : "NO" ,calculateAvgPoints());
    }
}
class LabExercises {
    private List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent (Student student){
        this.students.add(student);
    }
    public void printByAveragePoints (boolean ascending, int n){


        Comparator<Student> comparator = Comparator.comparing(Student::calculateAvgPoints)
                                        .thenComparing(Student::getIndex);
        if (!ascending){
            comparator = comparator.reversed();
        }
        students.stream()
                .sorted(comparator)
                .limit(n)
                .forEach(System.out::println);

    }
    public List<Student> failedStudents () {
        Comparator<Student> comparator = Comparator.comparing(Student::getIndex)
                .thenComparing(Student::calculateAvgPoints);

        return students.stream()
                .filter(s -> !s.getsSignature())
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    //2 : 5.91
    //3 : 4.68
    //4 : 3.28
    //5 : 4.06
    //6 : 2.89
    //7 : 4.07
    //8 : 3.10
    //9 : 4.30
    public Map<Integer,Double> getStatisticsByYear(){

        return students.stream()
                .filter(Student::getsSignature)
                .collect(Collectors.groupingBy(
                        Student::getYear,
                        Collectors.averagingDouble(Student::calculateAvgPoints)
                ));

    }
}