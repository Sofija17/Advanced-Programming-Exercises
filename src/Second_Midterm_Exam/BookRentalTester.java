package Second_Midterm_Exam;


import java.io.InputStream;
import java.util.*;



class BookRentalApp{
    private Map<String, List<String>> byBookName;
    private Map<String, List<String>> byUserId;

    public BookRentalApp() {
        this.byBookName = new HashMap<>();
        this.byUserId = new HashMap<>();
    }

    public void readData (InputStream is){
        Scanner sc = new Scanner(is);

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split(";");

            byBookName.putIfAbsent(parts[1], new ArrayList<>());
            byBookName.get(parts[1]).add(parts[0]);

            byUserId.putIfAbsent(parts[0], new ArrayList<>());
            byUserId.get(parts[0]).add(parts[1]);
        }
    }

    public int rentalsPerBook (String bookName){
        return byBookName.getOrDefault(bookName, Collections.emptyList()).size();
    }

    public int rentalsPerUser (String userId){
        return byUserId.get(userId).size();
    }

}


public class BookRentalTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookRentalApp app = new BookRentalApp();

        // прво ги читаме сите rentals до линијата ---
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("---")) {
                break;
            }
            sb.append(line).append("\n");
        }

        // пуштаме rentals податоци во readData
        InputStream is = new java.io.ByteArrayInputStream(sb.toString().getBytes());
        app.readData(is);

        // после --- очекуваме query
        if (sc.hasNextInt()) {
            int flag = sc.nextInt(); // 1 или 2
            sc.nextLine(); // потрошување на newline

            if (flag == 1) {
                // проверка за книга
                String bookName = sc.nextLine().trim();
                int result = app.rentalsPerBook(bookName);
                System.out.printf("The book %s was rented %d times;%n", bookName, result);
            } else if (flag == 2) {
                // проверка за user
                String userId = sc.nextLine().trim();
                int result = app.rentalsPerUser(userId);
                System.out.printf("The user %s has rented %d books;%n", userId, result);
            }
        }
    }
}




