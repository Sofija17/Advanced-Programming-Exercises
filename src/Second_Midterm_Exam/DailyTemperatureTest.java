package Second_Midterm_Exam;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class StatsPerDay {
    private int dayOfYear;
    private List<Double> temperatures;
    private char scale;

    public StatsPerDay(int dayOfYear, List<Double> temperatures, char scale) {
        this.dayOfYear = dayOfYear;
        this.temperatures = temperatures;
        this.scale = scale;
    }

    public static StatsPerDay createObject(String line) {
        //140 47F 49F 46F 46F 47F 49F 48F 50F 45F 47F 46F 49F 50F 47F 50F 49F 49F 47F 45F
        //299 18C 17C 17C 18C 16C 19C 16C 16C 17C 18C 21C 21C
        String[] parts = line.split("\\s+");

        int day = Integer.parseInt(parts[0]);
        char scale = parts[1].charAt(parts[1].length() - 1);
        List<Double> temperatures = new ArrayList<>();


        temperatures = Arrays.stream(parts).skip(1)
                .map(part -> part.substring(0, part.length() - 1))
                .mapToDouble(Double::parseDouble)
                .boxed()
                .collect(Collectors.toList());

        return new StatsPerDay(day, temperatures, scale);
    }

    public char getScale() {
        return scale;
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setScale(char scale) {
        this.scale = scale;
    }

    public List<Double> transformTemperatures(char scale) {
        List<Double> transformed = new ArrayList<>();

        if (scale == 'F') {
            transformed = temperatures.stream()
                    .mapToDouble(t -> (((t * 9) / (double) 5) + 32))
                    .boxed()
                    .collect(Collectors.toList());
        } else {
            transformed = temperatures.stream()
                    .mapToDouble(t -> (((t - 32) * 5) / (double) 9))
                    .boxed()
                    .collect(Collectors.toList());
        }

        return transformed;
    }

    public void setTemperatures(List<Double> temperatures) {
        this.temperatures = temperatures;
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics stats = temperatures.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        return String.format("%d: Count:%4d Min:%3.2f%s Max:%3.2f%s Avg:%3.2f%s",
                dayOfYear, stats.getCount(), stats.getMin(), scale, stats.getMax(), scale, stats.getAverage(), scale);
    }
}

class DailyTemperatures {
    private List<StatsPerDay> temperatures;

    public DailyTemperatures() {
        this.temperatures = new ArrayList<>();
    }

    public void readTemperatures(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        this.temperatures = br.lines()
                .map(StatsPerDay::createObject)
                .collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);

        for (int i = 0; i < temperatures.size(); i++) {
            StatsPerDay t = temperatures.get(i);
            if (t.getScale() != scale) {
                t.setTemperatures(t.transformTemperatures(scale));
                t.setScale(scale);
            }
        }
        Comparator<StatsPerDay> comparator = Comparator.comparing(StatsPerDay::getDayOfYear);

        temperatures.stream()
                .sorted(comparator)
                .forEach(pw::println);

        pw.flush();
    }

}