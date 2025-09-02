package Second_Midterm_Exam;

import com.sun.source.tree.Tree;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2], dtf);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        String nl = scanner.nextLine();
        LocalDateTime date = LocalDateTime.parse(nl, dtf);
//        LocalDateTime date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
// vashiot kod ovde

class WrongDateException extends Exception {
    public WrongDateException(LocalDateTime date) {
        //Thu Feb 14 11:00:00 UTC 2013
        super("Wrong date: " + date.format(
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'UTC' yyyy", Locale.ENGLISH)
        ));
    }
}

class Event implements Comparable<Event>{
    private String name;
    private String location;
    private LocalDateTime date;


    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    //19 Apr, 2012 15:30
    //19 Apr, 2012 15:30 at FINKI, Brucoshka Zabava
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm", Locale.ENGLISH);
        return date.format(formatter) + " at " + location + ", " + name;
    }


    @Override
    public int compareTo(Event o) {
        return Comparator.comparing(Event::getDate)
                .thenComparing(Event::getName)
                .compare(this,o);
    }
}

class EventCalendar {
    private int year;
    private List<Event> events;
    private Map<LocalDate, Set<Event>> eventsByDate;
    private Map<Integer, Long> numEventsPerMonth;


    public EventCalendar(int year) {
        this.year = year;
        this.events = new ArrayList<>();
        this.eventsByDate = new HashMap<>();
        this.numEventsPerMonth = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            numEventsPerMonth.put(i, 0L);
        }
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        Event e = new Event(name, location, date);

        if (date.getYear() != year) throw new WrongDateException(date);

        numEventsPerMonth.computeIfPresent(date.getMonth().getValue(), (k, v) -> v == 0 ? 1 : v + 1);

        events.add(e);
        eventsByDate.putIfAbsent(date.toLocalDate(), new TreeSet<>());
        eventsByDate.get(date.toLocalDate()).add(e);
    }

    public void listEvents(LocalDateTime date) {
        if (!eventsByDate.containsKey(date.toLocalDate())) {
            System.out.println("No events on this day!");
            return;
        }
        eventsByDate.get(date.toLocalDate()).forEach(System.out::println);
    }

    public void listByMonth() {
        //site meseci so brojot na nastani vo toj mesec
//        TreeMap<Integer, Long> map = this.events.stream()
//                .collect(Collectors.groupingBy(
//                        e -> e.getDate().getMonth().getValue(),
//                        TreeMap::new,
//                        Collectors.counting()
//                ));
//        map.entrySet()
//                .forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
        numEventsPerMonth.entrySet().
                forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
    }
}
