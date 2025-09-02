package Second_Midterm_Exam;

import java.io.InputStream;
import java.util.*;

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}

class QuizException extends Exception{
    public QuizException() {
        super("Ne mozhe");
    }
}

class StudentQuiz{
    private String index;
    private List<String> rightAnswers;
    private List<String> studentAnswers;

    private static final double CORRECT_ANSWER = 1;
    private static final double WRONG_ANSWER = -0.25;

    public StudentQuiz(String index, List<String> rightAnswers, List<String> studentAnswers) {
        this.index = index;
        this.rightAnswers = rightAnswers;
        this.studentAnswers = studentAnswers;
    }

    public static StudentQuiz createStudent(String line) throws QuizException {
        String [] parts = line.split(";");
        String idx = parts[0];

        List<String> rAnswers = new ArrayList<>();
        List<String> sAnswers = new ArrayList<>();

        String [] rA = parts[1].split(",");
        String [] sA = parts[2].split(",");

        if(rA.length != sA.length) throw new QuizException();

        for (int i=0; i<rA.length; i++){
            rAnswers.add(rA[i]);
            sAnswers.add(sA[i]);
        }

        return new StudentQuiz(idx,rAnswers, sAnswers);
    }

    public String getIndex() {
        return index;
    }

    public String calculateScore(){
        double points = 0.0;
        for (int i=0; i<studentAnswers.size(); i++){
            if (rightAnswers.get(i).equals(studentAnswers.get(i))){
                points+=CORRECT_ANSWER;
            } else {
                points += WRONG_ANSWER;
            }
        }
        return String.format("%.2f", points);
    }
}

class QuizProcessor {

    public static Map<String, Double> processAnswers(InputStream is) {
       List<StudentQuiz> quizAnswers = new ArrayList<>();
       Map<String,Double> byStudentIndex = new LinkedHashMap<>();

        Scanner sc = new Scanner(is);
        StudentQuiz s = null;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                s = StudentQuiz.createStudent(line);
            } catch (QuizException e) {
                System.out.println(e.getMessage());
            }

            if (s != null) {
                quizAnswers.add(s);
                byStudentIndex.putIfAbsent(s.getIndex(), Double.valueOf(s.calculateScore()));
            }
        }

        return byStudentIndex;
    }

}