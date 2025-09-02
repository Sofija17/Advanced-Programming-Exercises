package Second_Midterm_Exam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//Posledno baranje nedoresheno

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }

        }
    }
}


class CarAtParking {
    private String registration;
    private String spot;
    private LocalDateTime timestampEntered;
    private LocalDateTime timestampLeft;
    private boolean entry;
    private int numTimesParked;
    private boolean isParked;


    public CarAtParking(String registration, String spot, LocalDateTime timestampEntered, boolean entry) {
        this.registration = registration;
        this.spot = spot;
        this.timestampEntered = timestampEntered;
        this.entry = entry;
        this.timestampLeft = null;
        this.numTimesParked = 1;
        this.isParked = true;
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        this.registration = registration;
        this.spot = spot;
        if (!entry) {
            this.timestampLeft = timestamp;
            isParked = false;
        } else {
            this.timestampEntered = timestamp;
            this.numTimesParked++;
        }
        this.entry = entry;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public boolean isEntry() {
        return entry;
    }


    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public boolean isParked() {
        return isParked;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    public LocalDateTime getTimestampEntered() {
        return timestampEntered;
    }

    public void setTimestampEntered(LocalDateTime timestampEntered) {
        this.timestampEntered = timestampEntered;
    }

    public int getNumTimesParked() {
        return numTimesParked;
    }

    public LocalDateTime getTimestampLeft() {
        return timestampLeft;
    }

    public void setTimestampLeft(LocalDateTime timestampLeft) {
        this.timestampLeft = timestampLeft;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }

    public long calculateParkingDuration() {
        return DateUtil.durationBetween(timestampEntered, timestampLeft);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarAtParking that)) return false;
        return Objects.equals(registration, that.registration) && Objects.equals(spot, that.spot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, spot);
    }

    @Override
    public String toString() {
        //Registration number: NE0002AA Spot: A4 Start timestamp: 2024-01-15T21:11:32
        //Registration number: NE0003AA Spot: A7 Start timestamp: 2024-01-15T17:45:32 End timestamp: 2024-01-15T20:45:32 Duration in minutes: 180
        if (timestampLeft == null)
            return String.format("Registration number: %s Spot: %s Start timestamp: %s", registration, spot, timestampEntered);
        else
            return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d",
                    registration, spot, timestampEntered, timestampLeft, calculateParkingDuration());

    }
}

class Parking {
    private List<CarAtParking> parkedCars;
    private int capacity;
    private Map<String, List<CarAtParking>> bySpot;

    public Parking(int capacity) {
        this.capacity = capacity;
        this.parkedCars = new ArrayList<>();
        this.bySpot = new HashMap<>();
    }

    void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        Optional<CarAtParking> car = parkedCars.stream()
                .filter(c -> c.getRegistration().equals(registration))
                .findFirst();

        if (entry && car.isEmpty()) {
            CarAtParking newCar = new CarAtParking(registration, spot, timestamp, entry);
            parkedCars.add(newCar);

            bySpot.computeIfAbsent(spot, k -> new ArrayList<>());

            return;
        }

        bySpot.get(spot).add(car.get());
        car.get().update(registration, spot, timestamp, entry);
    }

    void currentState() {
        double countParkedCars = parkedCars.stream()
                .filter(CarAtParking::isEntry)
                .count();

        double percentFilled = countParkedCars * 100.0 / capacity;
        System.out.println(String.format("Capacity filled: %.2f%%", percentFilled));

        parkedCars.stream()
                .filter(CarAtParking::isEntry)
                .sorted(Comparator.comparing(CarAtParking::getTimestampEntered).reversed())
                .forEach(System.out::println);

    }

    void history() {
        parkedCars.stream()
                .filter(c -> !c.isParked())
                .sorted(Comparator.comparing(CarAtParking::calculateParkingDuration).reversed())
                .forEach(System.out::println);
    }

    Map<String, Integer> carStatistics() {

        Map<String, Integer> map = new TreeMap<>();
        for (CarAtParking c : parkedCars) {
            map.put(c.getRegistration(), c.getNumTimesParked());
        }
        return map;
    }

    private double calculateSpotOccupancy(double duration, long totalTimeInMinutes) {
        return (duration / totalTimeInMinutes) * 100.0;

    }

    private double calculateTimeCarWasParkedInParticularSpot(List<CarAtParking> car, LocalDateTime start, LocalDateTime end){
        double sumTime = 0.0;
        for (CarAtParking c : car){
            if (c.getTimestampEntered().isAfter(start) && c.getTimestampLeft().isBefore(end)){
                sumTime += c.calculateParkingDuration();
            }
        }
        return sumTime;
    }

    Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        long totalTimeInMinutes = DateUtil.durationBetween(start, end);
        Map<String, Double> spotMap = new HashMap<>();


        Map<String, Double> map = bySpot.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateTimeCarWasParkedInParticularSpot(entry.getValue(), start, end)
                ));

        return map.entrySet().stream()
                .filter(entry -> entry.getValue() > 0.0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateSpotOccupancy(entry.getValue(), totalTimeInMinutes)
                ));




//        HashMap<String, Double> carsParkedInThisTimestamp = parkedCars.stream()
//                .filter(c -> c.getTimestampEntered().isAfter(start) && c.getTimestampLeft().isBefore(end))
//                .collect(Collectors.groupingBy(
//                        CarAtParking::getSpot,
//                        HashMap::new,
//                        Collectors.summingDouble(CarAtParking::calculateParkingDuration)
//                ));
//
//        for (Map.Entry<String, Double> entry : carsParkedInThisTimestamp.entrySet()) {
//            entry.setValue(calculateSpotOccupancy(entry.getValue(), totalTimeInMinutes));
//        }
//        return carsParkedInThisTimestamp;
    }
}
