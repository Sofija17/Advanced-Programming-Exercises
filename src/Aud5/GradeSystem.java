package Aud5;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Student implements Comparable<Student>{
    private String firstname;
    private String lastname;
    private int exam1;
    private int exam2;
    private int exam3;

    public Student(String firstname, String lastname, int exam1, int exam2, int exam3) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.exam3 = exam3;
    }

    //LastName:FirstName:Exam1:Exam2:Exam3
    public Student(String line){
        String[] parts = line.split(":");
        this.lastname = parts[0];
        this.firstname = parts[1];
        this.exam1 = Integer.parseInt(parts[2]);
        this.exam2 = Integer.parseInt(parts[3]);
        this.exam3 = Integer.parseInt(parts[4]);
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public double totalPoints(){
        return exam1*0.25 + exam2*0.3 + exam3*0.45;
    }

    public String gradeCalculation(){
        double points = totalPoints();
        char grade=' ';

        if(points >= 90)
            grade='A';
        else if (points >= 80)
            grade='B';
        else if (points >= 70)
            grade='C';
        else if (points >= 60)
            grade='D';
        else
            grade='F';

        return Character.toString(grade);
    }

    @Override
    public String toString() {

        return String.format("%s %s %s ", lastname, firstname, gradeCalculation());
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(this.totalPoints(),other.totalPoints());
    }

    public String printDetailedData (){
        //LastName FirstName Exam1 Exam2 Exam3 TotalPoints LetterGrade
        return this.lastname + " " + this.firstname + " " + this.exam1 + " "
                + this.exam2 + " " + this.exam3 + " " + this.totalPoints()
                + " " + this.gradeCalculation();
    }
}

class Course {
    List<Student> students;

    public Course() {
        this.students = new ArrayList<>();
    }

    //Chita od input stream
    public void readData(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        this.students = br.lines().map(line -> new Student(line)).toList();

    }

    //Printa na ekran
    public void printSortedData (OutputStream os){
        PrintWriter ps = new PrintWriter(os);

        this.students.stream()
                .sorted()
                .forEach(ps::println);

        ps.flush();
        ps.close();

    }

    //Zapishuva vo output stream
    public void printDetailedData(OutputStream os){
        PrintWriter ps = new PrintWriter(os);


        this.students.stream()
                .forEach(s -> ps.println(s.printDetailedData()));

        ps.flush();
        ps.close();
    }

}


public class GradeSystem {


    public static void main(String[] args) throws FileNotFoundException {

        File is = new File("src/Aud5/Grades");
        File os = new File("src/Aud5/OutputResults");

        Course course = new Course();

        course.readData(new FileInputStream(is));
        System.out.println("Printing sorted data");
        course.printSortedData(System.out);

        System.out.println("Printing data to output stream");
        course.printDetailedData(new FileOutputStream(os));

    }
}
