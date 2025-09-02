package Second_Midterm_Exam;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here

class StudentRecord {
    private String code;
    private String nasoka;
    private List<Integer> grades;

    public static final Comparator<StudentRecord> STUDENT_RECORD_COMPARATOR = Comparator.comparing(StudentRecord::calculateAverageGrade).reversed()
            .thenComparing(StudentRecord::getCode);

    public StudentRecord(String code, String nasoka, List<Integer> grades) {
        this.code = code;
        this.nasoka = nasoka;
        this.grades = grades;
    }

    public static StudentRecord createRecord(String line) {
        String[] parts = line.split("\\s+");
        String code = parts[0];
        String nasoka = parts[1];

        List<Integer> grades = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            grades.add(Integer.parseInt(parts[i]));
        }

        return new StudentRecord(code, nasoka, grades);
    }

    public double calculateAverageGrade() {
        return grades.stream()
                .mapToDouble(Double::valueOf)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, calculateAverageGrade());
    }

    public String getCode() {
        return code;
    }

    public String getNasoka() {
        return nasoka;
    }

    public List<Integer> getGrades() {
        return grades;
    }

}

class GradeDistribution {
    private List<StudentRecord> records;
    private List<Integer> grades;
    private long six;
    private long seven;
    private long eight;
    private long nine;
    private long ten;

    public GradeDistribution(List<StudentRecord> records) {
        this.records = records;
        this.grades = new ArrayList<>();
        this.six = 0;
        this.eight = 0;
        this.nine = 0;
        this.ten = 0;
        this.seven = 0;

        unpackGrades();
    }

    public void unpackGrades() {
        records.stream()
                .forEach(record -> this.grades.addAll(record.getGrades()));
        countGrades();
    }

    public void countGrades() {
        for (int g : grades) {
            switch (g) {
                case 6:
                    six++;
                    break;
                case 7:
                    seven++;
                    break;
                case 8:
                    eight++;
                    break;
                case 9:
                    nine++;
                    break;
                case 10:
                    ten++;
                    break;
            }
        }

    }
    //KNI
    // 6 | *******************(181)
    // 7 | ************************(237)
    // 8 | ***************************(269)
    // 9 | ************************(236)
    //10 | *****************(163)
    //IKI

    public String print(long count) {
        int stars = (int) Math.ceil(count / 10.0);
        return "*".repeat(stars);
    }


    public long getTen() {
        return ten;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%2d | %s(%d)%n", 6, print(six), six));
        sb.append(String.format("%2d | %s(%d)%n", 7, print(seven), seven));
        sb.append(String.format("%2d | %s(%d)%n", 8, print(eight), eight));
        sb.append(String.format("%2d | %s(%d)%n", 9, print(nine), nine));
        sb.append(String.format("%2d | %s(%d)",   10, print(ten), ten));

        return sb.toString();
    }
}

class StudentRecords {
    private List<StudentRecord> studentRecords;
    private Map<String, Set<StudentRecord>> byNasoka;
    private Map<String, List<StudentRecord>> distributionByNasoka;

    public StudentRecords() {
        this.studentRecords = new ArrayList<>();
        this.byNasoka = new TreeMap<>();
        this.distributionByNasoka = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            StudentRecord r = StudentRecord.createRecord(line);

            studentRecords.add(r);
            byNasoka.putIfAbsent(r.getNasoka(), new TreeSet<>(StudentRecord.STUDENT_RECORD_COMPARATOR));
            byNasoka.get(r.getNasoka()).add(r);

            distributionByNasoka.putIfAbsent(r.getNasoka(), new ArrayList<>());
            distributionByNasoka.get(r.getNasoka()).add(r);
        }

        return studentRecords.size();
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        byNasoka.entrySet()
                .forEach(entry -> {
                    pw.println(entry.getKey());
                    entry.getValue().forEach(pw::println);
                });

        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        distributionByNasoka.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), new GradeDistribution(entry.getValue())))
                .sorted((e1, e2) -> Long.compare(e2.getValue().getTen(), e1.getValue().getTen()))
                .forEach(entry -> {
                    pw.println(entry.getKey());
                    pw.println(entry.getValue());
                });

        pw.flush();
    }
}
