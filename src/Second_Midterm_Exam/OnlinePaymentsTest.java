package Second_Midterm_Exam;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.rmi.StubNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Collections.sort;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> {
            try {
                onlinePayments.printStudentReport(id, System.out);
            } catch (StudentNotFoundExc e) {
                System.out.println(e.getMessage());
            }
        });
    }
}


class PaymentItem implements Comparable<PaymentItem>{
    private String item;
    private int price;

    public PaymentItem(String item, int price) {
        this.item = item;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
       return item + " " + price;
    }

    @Override
    public int compareTo(PaymentItem o) {
        return Comparator.comparing(PaymentItem::getPrice).reversed().compare(this,o);
    }
}

class StudentNotFoundExc extends Exception{
    public StudentNotFoundExc(String idx) {
        super(String.format("Student %s not found!", idx));
    }
}

class OnlinePayments{
    private Map<String, List<PaymentItem>> paymentsByStudent;

    private static final double FEE = 1.14;

    public OnlinePayments() {
        this.paymentsByStudent = new HashMap<>();
    }

    void readItems (InputStream is){
        Scanner sc = new Scanner(is);

        while (sc.hasNextLine()){
            String line = sc.nextLine();

            String [] parts = line.split(";");
            PaymentItem p = new PaymentItem(parts[1], Integer.parseInt(parts[2]));

            this.paymentsByStudent.putIfAbsent(parts[0], new ArrayList<>());
            this.paymentsByStudent.get(parts[0]).add(p);
        }
    }


    void printStudentReport (String index, OutputStream os) throws StudentNotFoundExc {
        PrintWriter pw = new PrintWriter(os);

        if(paymentsByStudent.get(index)==null)
            throw new StudentNotFoundExc(index);

        List<PaymentItem> payments = paymentsByStudent.get(index);

        int netoAmount = payments.stream()
                .mapToInt(PaymentItem::getPrice)
                .sum();

        long fee = Math.round((FEE/100.0) * netoAmount);

        if(fee < 3) fee=3;
        if (fee > 300) fee=300;

        long totalAmount = netoAmount + fee;

        pw.println(String.format("Student: %s Net: %d Fee: %d Total: %d",index, netoAmount, fee, totalAmount));
        pw.println("Items: ");

        sort(payments);
        int k=1;
        for (int i=0; i<payments.size(); i++){
            pw.println(k++ + ". " + payments.get(i));
        }

        pw.flush();
    }

}
