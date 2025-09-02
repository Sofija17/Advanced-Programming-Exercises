//package Second_Midterm_Exam;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//class CosineSimilarityCalculator {
//    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
//        int[] array1;
//        int[] array2;
//        array1 = c1.stream().mapToInt(i -> i).toArray();
//        array2 = c2.stream().mapToInt(i -> i).toArray();
//        double up = 0.0;
//        double down1 = 0, down2 = 0;
//
//        for (int i = 0; i < c1.size(); i++) {
//            up += (array1[i] * array2[i]);
//        }
//
//        for (int i = 0; i < c1.size(); i++) {
//            down1 += (array1[i] * array1[i]);
//        }
//
//        for (int i = 0; i < c1.size(); i++) {
//            down2 += (array2[i] * array2[i]);
//        }
//
//        return up / (Math.sqrt(down1) * Math.sqrt(down2));
//    }
//}
//
//class Line {
//    private List<String> line;
//    private List<Integer> vector;
//    private String textLine;
//
//    public Line(List<String> line, String textLine) {
//        this.line = line;
//        this.textLine = textLine;
//        int length = line.size();
//        this.vector = new ArrayList<>();
//
//    }
//
//    public String getTextLine() {
//        return textLine;
//    }
//
//    public List<String> getLine() {
//        return line;
//    }
//
//    public List<Integer> getVector() {
//        return vector;
//    }
//
//    public void setVector(List<Integer> vector) {
//        this.vector = vector;
//    }
//
//    private static String cleanText(String line) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < line.length(); i++) {
//            char c = line.charAt(i);
//            if (Character.isLetter(c) || Character.isWhitespace(c))
//                sb.append(Character.toLowerCase(c));
//        }
//        return sb.toString();
//    }
//
//    public static Line createLine(String line) {
//        String cleanedLine = cleanText(line);
//        String rawLine = line;
//
//        List<String> textLine = new ArrayList<>();
//        String[] parts = cleanedLine.split("\\s+");
//
//        textLine.addAll(Arrays.asList(parts));
//
//        return new Line(textLine, rawLine);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("[ ");
//        for (int i = 0; i < vector.size(); i++) {
//            if (i == vector.size() - 1)
//                sb.append(vector.get(i));
//
//            sb.append(vector.get(i)).append(", ");
//        }
//        sb.append(" ]");
//        return sb.toString();
//    }
//}
//
//class TextProcessor {
//    private Map<String, Integer> wordMap;
//    private List<Line> lines;
//
//    public TextProcessor() {
//        this.wordMap = new TreeMap<>();
//        this.lines = new ArrayList<>();
//    }
//
//    public void readText(InputStream in) {
//        Scanner scanner = new Scanner(in);
//
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            Line textLine = Line.createLine(line);
//
//            this.lines.add(textLine);
//            for (int i = 0; i < textLine.getLine().size(); i++) {
//                String key = textLine.getLine().get(i);
//                this.wordMap.compute(key, (k, v) -> v == null ? 1 : v + 1);
//            }
//        }
//
//    }
//
//    private List<Integer> createLineVector(Line textLine) {
//        List<Integer> vector = new ArrayList<>(wordMap.size());
//
//
//        Map<String, Long> frequencies = textLine.getLine().stream()
//                .collect(Collectors.groupingBy(
//                        w -> w,
//                        Collectors.counting()
//                ));
//
//
//        for (String key : wordMap.keySet()) {
//            if (textLine.getLine().contains(key)) {
//                vector.add(frequencies.get(key).intValue());
//            } else {
//                vector.add(0);
//            }
//        }
//
//        textLine.setVector(vector);
//        return vector;
//    }
//
//    public void printTextsVectors(PrintStream out) {
//        PrintWriter pw = new PrintWriter(out);
//
//        lines.stream()
//                .map(this::createLineVector)
//                .forEach(pw::println);
//
//        pw.flush();
//    }
//
//    public void printCorpus(PrintStream out, int n, boolean ascending) {
//        PrintWriter pw = new PrintWriter(out);
//
//        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(wordMap.entrySet());
//
//        Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
//
//        if (ascending) {
//            sorted.sort(comparator);
//        } else {
//            sorted.sort(comparator.reversed());
//        }
//
//        sorted.stream()
//                .limit(n)
//                .forEach(entry -> {
//                    pw.println(entry.getKey() + " : " + entry.getValue());
//                });
//
//        pw.flush();
//    }
//
//    public void mostSimilarTexts(PrintStream out) {
//        PrintWriter pw = new PrintWriter(out);
//
//        double similarity = 0.0;
//        double maxSim = -1.0;
//        Line best1 = null, best2 = null;
//        for (int i = 0; i < lines.size(); i++) {
//            for (int j = i + 1; j < lines.size(); j++) {
//                similarity = CosineSimilarityCalculator.cosineSimilarity(
//                        lines.get(i).getVector(),
//                        lines.get(j).getVector());
//
//                if (similarity > maxSim) {
//                    maxSim = similarity;
//                    best1 = lines.get(i);
//                    best2 = lines.get(j);
//                }
//            }
//        }
//        pw.println(best1.getTextLine());
//        pw.println(best2.getTextLine());
//        pw.println(String.format("%.10f", maxSim));
//
//        pw.flush();
//    }
//}
//
//public class TextProcessorTest {
//
//    public static void main(String[] args) {
//        TextProcessor textProcessor = new TextProcessor();
//
//        textProcessor.readText(System.in);
//
//        System.out.println("===PRINT VECTORS===");
//        textProcessor.printTextsVectors(System.out);
//
//        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
//        textProcessor.printCorpus(System.out, 20, true);
//
//        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
//        textProcessor.printCorpus(System.out, 20, false);
//
//        System.out.println("===MOST SIMILAR TEXTS===");
//        textProcessor.mostSimilarTexts(System.out);
//    }
//}