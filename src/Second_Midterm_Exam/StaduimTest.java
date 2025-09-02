package Second_Midterm_Exam;

import java.util.*;

class SeatTakenException extends Exception {
    public SeatTakenException(String message) {
        super(message);
    }
}

class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException(String message) {
        super(message);
    }
}

class Sector implements Comparable<Sector> {
    private String code;
    private int numSeats;
    private int type;
    private Map<Integer, Boolean> isSeatTaken;

    private static final Comparator<Sector> SECTOR_COMPARATOR = Comparator
            .comparing(Sector::freeSeats)
            .reversed()
            .thenComparing(Sector::getCode);

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;
        this.type = 0;
        this.isSeatTaken = new HashMap<>();

        for (int i = 1; i <= numSeats; i++) {
            isSeatTaken.put(i, false); //na pochetok site sedista se slobodni
        }
    }

    public int freeSeats() {
        int count = 0;
        for (Map.Entry<Integer, Boolean> entry : isSeatTaken.entrySet()) {
            if (!entry.getValue())
                count++;
        }
        return count;
    }


    public boolean getSeatInfo(Integer num) {
        return isSeatTaken.get(num);
    }

    public String getCode() {
        return code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIsSeatTaken(int num) {
        this.isSeatTaken.replace(num, true);
    }

    public double percentFreeSeats() {
        return ((numSeats - freeSeats()) * 100.0) / numSeats;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, freeSeats(), numSeats, percentFreeSeats());
    }

    @Override
    public int compareTo(Sector o) {
        return SECTOR_COMPARATOR.compare(this, o);
    }
}

class Stadium {
    private String name;
    private List<Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new ArrayList<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for (int i = 0; i < sectorSizes.length; i++) {
            sectors.add(new Sector(sectorNames[i], sectorSizes[i]));
        }
    }

    public void buyTicket(String sectorCode, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector sector = sectors.stream().
                filter(s -> s.getCode().equals(sectorCode))
                .findFirst()
                .orElse(null);

        if (sector == null) return;

        if (sector.getSeatInfo(seat))
            throw new SeatTakenException("SeatTakenException");

        if (type != 0) {
            if (sector.getType() == 0)
                sector.setType(type);
        }
        if (type == 1 && sector.getType() == 2) throw new SeatNotAllowedException("SeatNotAllowedException");
        if (type == 2 && sector.getType() == 1) throw new SeatNotAllowedException("SeatNotAllowedException");


        sector.setIsSeatTaken(seat);
    }

    public void showSectors() {
        sectors.stream()
                .sorted()
                .forEach(System.out::println);
    }
}


public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}   