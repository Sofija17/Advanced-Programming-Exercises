package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super("Duplicate number: " + number);
    }
}

class Contact{
    private String name;
    private String number;

    static final Comparator<Contact> COMPARATOR =
            Comparator.comparing(Contact::getName)
            .thenComparing(Contact::getNumber);

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}

class PhoneBook{
    private Map<String, Contact> byNumber;
    private Map<String, Set<Contact>> partialNumToContact;
    private Map<String, Set<Contact>> byName;

    public PhoneBook() {
        byNumber = new HashMap<>();
        partialNumToContact = new HashMap<>();
        byName = new HashMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        Contact c = new Contact(name,number);

        if(byNumber.containsKey(number))
            throw new DuplicateNumberException(number);

        byNumber.put(number, c);

        byName.putIfAbsent(name, new TreeSet<>(Contact.COMPARATOR));
        byName.get(name).add(c);

        List<String> subNumbers = getNumberParts(number);
        for (String subNumber : subNumbers){
            partialNumToContact.putIfAbsent(subNumber, new TreeSet<>(Contact.COMPARATOR));
            partialNumToContact.get(subNumber).add(c);
        }
    }

    List<String> getNumberParts(String number){
        List<String> list = new ArrayList<>();

        for (int len=3; len<=number.length(); len++){
            for (int start=0; start <= number.length()-len; start++){
                list.add(number.substring(start, start + len));
            }
        }
        return list;
    }

    void contactsByNumber(String number){
        if(!partialNumToContact.containsKey(number)) {
            System.out.println("NOT FOUND");
            return;
        }
        partialNumToContact.get(number).forEach(System.out::println);
    }

    void contactsByName(String name){
        if(!byName.containsKey(name)) {
            System.out.println("NOT FOUND");
            return;
        }

        byName.get(name).forEach(System.out::println);
    }

}





