package First_Midterm;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class  NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

abstract class Archive{
    protected int id;
    protected LocalDate dateArchived;
    protected int numOpens;
    protected LocalDate dateOpened;

    public Archive(int id) {
        this.id = id;
    }

    public Archive(int id, LocalDate dateArchived) {
        this.id = id;
        this.dateArchived = dateArchived;
        this.numOpens=0;
        this.dateOpened=null;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }


    public int getNumOpens() {
        return numOpens;
    }

    abstract String tryOpen(int id, LocalDate date);
}

class LockedArchive extends Archive{
    private LocalDate dateToOpen;


    public LockedArchive(int id, LocalDate dto) {
        super(id);
        this.dateToOpen = dto;
    }

    @Override
    String tryOpen(int id, LocalDate date) {
        if(date.isAfter(dateToOpen)) {
            dateOpened=date;
            numOpens++;
            return String.format("Item %d opened at %s", id, dateOpened);
        }
        return String.format("Item %d cannot be opened before %s", id, dateToOpen);
    }
}

class SpecialArchive extends Archive{
    private int maxOpen;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
    }


    @Override
    String tryOpen(int id, LocalDate date) {
        if(numOpens < maxOpen){
            dateOpened=date;
            numOpens++;
            return String.format("Item %d opened at %s", id, dateOpened);
        }
        return String.format("Item %d cannot be opened more than %d times", id, maxOpen);
    }
}

class ArchiveStore {
    private List<Archive> archives;
    private StringBuilder log;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.log = new StringBuilder();
    }

    void archiveItem(Archive item, LocalDate date){
        item.dateArchived = date;
        archives.add(item);
       log.append("Item " + item.getId() + " archived at " + date +"\n");
    }

    void openItem(int id, LocalDate date) throws NonExistingItemException {
        //da ja najdam arhivata so toj id i da i napraam open
        Archive arc = archives.stream()
                .filter(a -> a.getId()==id)
                .findFirst()
                .orElseThrow(() -> new NonExistingItemException(id));

        String msg = arc.tryOpen(id,date);
        log.append(msg).append(System.lineSeparator());
    }

    public String getLog() {
        return log.toString();
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}