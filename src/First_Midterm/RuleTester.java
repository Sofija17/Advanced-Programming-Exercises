package First_Midterm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    public double averageGrade() {
        return grades.stream().mapToDouble(g -> g.doubleValue())
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}


class Rule<T, E> {

    private Function<T, E> function;
    private Predicate<T> predicate;

    public Rule(Predicate<T> predicate, Function<T, E> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public Optional<E> apply(T input) {
        //ako ne e ispolnet predicate vrakja false
        //vo sprotivno go vrakja rezultatot od function
        if (predicate.test(input)) {
            System.out.println("Result: " + Optional.of(function.apply(input)).get());
            return Optional.of(function.apply(input));
        }

        System.out.println("Condition not met");
        return Optional.empty();
    }
}

class RuleProcessor<E> {

    //Методот потребно е врз секој елемент од листата на влезни податоци
    // да го примени секое правило од листата на правила и на екран да го
    // испечати резултатот од примената на правилото (доколку постои),
    // а во спротивно да испечати порака Condition not met.
    public static <T, E> void process(List<T> inputData, List<Rule<T,E>> rules) {
        Optional<E> result=null;
        for (T element: inputData){
            System.out.println("Input: " + element);
            for (Rule <T,E> rule : rules){
                result = rule.apply(element);
            }

            }
        }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            rules.add(new Rule<>(
                    s -> s.contains("NP"),
                    s -> s.indexOf("NP")
            ));

            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */

            rules.add(new Rule<>(
                    s -> s.startsWith("NP"),
                    s -> s.length()
            ));


            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            rules.add(new Rule<>(
                    s -> s.grades.size() >= 3,
                    s -> s.grades.stream().
                            max((a, b) -> a.compareTo(b)).
                            orElse(0) * 1.0
            ));


            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            rules.add(new Rule<>(
                    s -> s.id.startsWith("20"),
                    s -> s.grades.isEmpty() ? 5.0 : s.averageGrade()
            ));


            List<Student> students = new ArrayList<>();
            while (sc.hasNext()) {
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}

