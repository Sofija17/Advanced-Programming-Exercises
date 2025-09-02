package Second_Midterm_Exam;

//package Second_Midterm_Exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test
 */

class Word {
    private String word;
    private List<Integer> vector;

}

class WordVectors {
    private String[] words;
    private List<List<Integer>> vectors;
    private Map<String, List<Integer>> wordToVector;
    private List<String> readWords;


    private List<Integer> summarizedVector = new ArrayList<>(Arrays.asList(0,0,0,0,0));

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.words = words;
        this.vectors = vectors;
        this.readWords = null;
        this.wordToVector = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            List<Integer> word_vector = vectors.get(i);

            wordToVector.putIfAbsent(word, new ArrayList<>(word_vector));
        }
    }


    public void readWords(List<String> wordsList) {

        this.readWords = wordsList;
        List<Integer> defaultVector = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5));

        wordsList.forEach(word -> {
            wordToVector.putIfAbsent(word, new ArrayList<>(defaultVector));
        });
    }

    public void summarizeVectors(List<Integer> vector){

        for (int i=0; i<vector.size(); i++){
            int sum = summarizedVector.get(i) + vector.get(i);
            summarizedVector.set(i, sum);
        }
    }

    public List<Integer> slidingWindow(int n) {
        List <Integer> wordSums = new ArrayList<>();

        for (int i=0; i+n <= readWords.size(); i++){
            for (int j=i; j<n+i; j++){
                List<Integer> vector = wordToVector.get(readWords.get(j));
                summarizeVectors(vector);
            }
            int sum = summarizedVector.stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0);
            wordSums.add(sum);
            summarizedVector = new ArrayList<>(Arrays.asList(0,0,0,0,0));
        }
        return wordSums;
    }

}


public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}




