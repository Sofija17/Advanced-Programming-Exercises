package Second_Midterm_Exam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static List<String>getNumberParts(String number){
        List<String> list = new ArrayList<>();

        for (int len=3; len<=number.length(); len++){
            for (int start=0; start <= number.length()-len; start++){
                list.add(number.substring(start, start + len));
            }
        }
        return list;

    }

    public static void main(String[] args) {
        List<String> list = getNumberParts("077446818");
        System.out.println(list);
    }
}
