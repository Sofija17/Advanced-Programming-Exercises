package First_Midterm;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception {


    public InvalidOperationException(String answer) {
        super(String.format("%s is not allowed option for this question", answer));
    }

    public InvalidOperationException() {
        super("Answers and questions must be of same length!");
    }
}

abstract class Question implements Comparable<Question> {

    protected String text;
    protected int points;
    protected String answer;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    abstract double calculatePoints(String correctAnswer);

    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.points, other.points);
    }

}

class MultipleQuestion extends Question {

    private boolean checkAnswerLetter(String answer) {
        return ((answer.equals("A") || answer.equals("B")
                || answer.equals("C") || answer.equals("D")
                || answer.equals("E")));

    }

    public MultipleQuestion(String text, int points, String answer) throws InvalidOperationException {
        super(text, points);

        if (!checkAnswerLetter(answer))
            throw new InvalidOperationException(answer);

        this.answer = answer;

    }

    public double calculatePoints(String correctAnswer) {
        if (!this.answer.equals(correctAnswer)) {
            return points*0.2*(-1);
        }
        return points;

    }

    @Override
    public String toString() {
        return "Multiple Choice Question: " + super.text +
                " Points " + super.points + " Answer: " + this.answer;
    }

}

class TFQuestion extends Question {

    public TFQuestion(String text, int points, String answer) {
        super(text, points);
        this.answer = answer;
    }

    public double calculatePoints(String correctAnswer) {
        if (!this.answer.equals(correctAnswer))
            return 0.00;

        return points;

    }

    @Override
    public String toString() {
        return "True/False Question: " + super.text +
                " Points: " + super.points + " Answer: " + this.answer;
    }
}

class Quiz {

    private List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    //MC;Question1;3;E
    public void addQuestion(String questionData) {

        String[] parts = questionData.split(";");

        if (parts[0].equals("MC")) {
            Question MCQuestion = null;
            try {
                MCQuestion = new MultipleQuestion(
                        parts[1],
                        Integer.parseInt(parts[2]),
                        parts[3]

                );
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
            if (MCQuestion != null)
                questions.add(MCQuestion);

        } else {
            Question TFQuestion = new TFQuestion(
                    parts[1],
                    Integer.parseInt(parts[2]),
                    parts[3]
            );
            questions.add(TFQuestion);
        }

    }

    public void printQuiz(PrintStream out) {

        PrintWriter pw = new PrintWriter(out);

        questions.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(q -> pw.println(q.toString()));

        pw.flush();
    }

    public void answerQuiz(List<String> answers, PrintStream out) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(out);

        double totalPoints = 0.0;

        if (answers.size() != questions.size()) {
            throw new InvalidOperationException();
        }

        for (int i = 0; i < questions.size(); i++) {

            Question q = questions.get(i);

            totalPoints += q.calculatePoints(answers.get(i));
            pw.println(String.format("%d. %.2f", i+1 ,q.calculatePoints(answers.get(i))));
        }
        pw.println(String.format("Total points: %.2f", totalPoints));

        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
