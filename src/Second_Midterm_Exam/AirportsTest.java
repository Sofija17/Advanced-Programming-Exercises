package Second_Midterm_Exam;

import com.sun.source.tree.Tree;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;
    private List<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}

class Flight{
    private String from;
    private String to;
    private int time;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String calculateTime(){
        int hours = (time / 60) % 24;
        int minutes = time % 60;

        //arrival time:
        int h = (duration / 60) % 24;
        int m = duration % 60;

        int arrivalH = hours+h;
        int arrivalM = minutes+m;

        if (arrivalH > 24){
            arrivalH = arrivalH%24;
        }

        int durationHours = arrivalH - hours;
        int durationMinutes = arrivalM - minutes;

        return String.format("%02d:%02d-%02d:%02d %02dh:%02dm", hours,minutes, arrivalH,arrivalM, durationHours,durationMinutes);
    }


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        //LHR-ATL 12:44-17:35 4h51m
        return String.format(" %s ",calculateTime());
    }
}

class Airports{
    private List<Airport> airports;
    private Map<String, TreeMap<String,Flight>> airportMap;

    public Airports() {
        this.airports = new ArrayList<>();
        this.airportMap = new TreeMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        this.airports.add(new Airport(name,country,code,passengers));

        this.airportMap.putIfAbsent(code,new TreeMap<>());
    }

    public void addFlights(String from, String to, int time, int duration){
        Flight f = new Flight(from,to,time,duration);

        Airport airport = airports.stream().filter(a -> a.getCode().equals(from)).findAny().get();
        airport.getFlights().add(f);

        airportMap.putIfAbsent(from, new TreeMap<>());
        airportMap.get(from).putIfAbsent(to, f);

    }

    public void showFlightsFromAirport(String code){
       Airport airport = airports.stream().filter(a -> a.getCode().equals(code)).findFirst().get();

        System.out.println(String.format("%s (%s)", airport.getName(), airport.getCode()));
        System.out.println(airport.getCountry());
        System.out.println(airport.getPassengers());

        airportMap.get(code).entrySet().stream()
                .forEach(e -> {
                    System.out.println(code + "-" + e.getKey() + e.getValue().toString());
                });
    }
    public void showDirectFlightsFromTo(String from, String to){
        airportMap.get(from).entrySet().stream()
                .forEach(e -> System.out.println(e.getValue()));
    }
    public void showDirectFlightsTo(String to){
        airportMap.entrySet().stream()
                .forEach(entry -> entry.getValue().entrySet().stream()
                        .filter(f -> f.getValue().getTo().equals(to))
                        .forEach(f -> System.out.println(f.getValue().getFrom() + "-" + f))


                );

    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


