package First_Midterm;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Runner implements Comparable<Runner> {
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

//    public int calculateTimeDifference() {
//        return endTime.toSecondOfDay() - startTime.toSecondOfDay();
//    }

    public Duration duration(){
        return Duration.between(startTime,endTime);
    }

    public static String transformSecondsToTimesamp(Duration d) {

//        int hours = totalSeconds / 3600;
//        int rem = totalSeconds % 3600;
//        int minutes = rem / 60;
//        int seconds = rem % 60;
        long hours = d.toHours();
        int minutes = d.toMinutesPart();
        int seconds = d.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Runner(int id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Runner createRunner(String line) {
        String[] parts = line.split(" ");
        int id = Integer.parseInt(parts[0]);
        LocalTime start = LocalTime.parse(parts[1]);
        LocalTime end = LocalTime.parse(parts[2]);

        return new Runner(id, start, end);

    }

    //1002 00:35:12
    //02:39:54
    @Override
    public String toString() {
        return id + " " + transformSecondsToTimesamp(this.duration());
    }

    @Override
    public int compareTo(Runner other) {
        return this.duration().compareTo(other.duration());
    }
}

class TeamRace {

    //ID START_TIME END_TIME (пр. 1234 08:00:05 08:31:26)
    public static void findBestTeam(InputStream is, OutputStream os) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter pw = new PrintWriter(os);

        List<Runner> runners = br.lines()
                .map(line -> Runner.createRunner(line))
                .collect(Collectors.toList());

        runners = runners.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        Duration totalTime = Duration.ZERO;

        for (int i = 0; i < 4; i++) {
            pw.println(runners.get(i));
            totalTime = totalTime.plus(runners.get(i).duration());
        }

        pw.println(Runner.transformSecondsToTimesamp(totalTime));

        pw.flush();
    }
}


public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}