package Aud2;


//Nedoreshena posledniot del za toString
public class Date implements Comparable<Date> {

    private final int days;

    private static int FIRST_YEAR = 1800;
    private static int LAST_YEAR = 2500;
    private static int DAYS_IN_YEAR = 356;

    private static final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] daysTillFirstOfMonth;

    //Chuva kolku denovi ima do sekoja sledna god pocnuvajki od 1 jan 1800: 0, 365, 730...
    private static final int[] daysTillFirstOfYear;


    //statichki blok za inicijalizacija na static promenlivi pri start na programata
    static {
        daysTillFirstOfMonth = new int[12];
        for (int i = 1; i < 12; i++) {
            daysTillFirstOfMonth[i] += daysTillFirstOfMonth[i - 1] + daysOfMonth[i - 1];
        }

        int total_years = LAST_YEAR - FIRST_YEAR + 1;
        int currentYear = FIRST_YEAR;
        daysTillFirstOfYear = new int [total_years];
        for (int i = 1; i < total_years; i++) {
            if (isLeapYear(currentYear)) {
                daysTillFirstOfYear[i] = daysTillFirstOfYear[i - 1] + DAYS_IN_YEAR + 1;
            } else {
                daysTillFirstOfYear[i] = daysTillFirstOfYear[i - 1] + DAYS_IN_YEAR;
            }
            currentYear++;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
    }


    public Date(int days) {
        this.days = days;
    }

    public Date(int date, int month, int year) {
        int days = 0;
        if (year < 1800 || year > 2500)
            throw new RuntimeException();

        //datumot shto kje go primam da go pretvoram vo broj na denovi samo
        days += daysTillFirstOfYear[year - FIRST_YEAR];
        days += daysTillFirstOfMonth[month - 1];

        if (month > 2 && isLeapYear(year)) {
            days++;
        }
        days += date;
        this.days = days;
    }

    public int subtract (Date date){
        return this.days - date.days;
    }

    public Date increment (int days){
        return new Date(this.days + days);
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//    }

    @Override
    public int compareTo(Date o) {
        return Integer.compare(this.days,o.days);
    }
    public static void main(String[] args) {
        Date sample = new Date(1, 10, 2012);
        System.out.println(sample.subtract(new Date(1, 1, 2000)));
        System.out.println(sample);
        sample = new Date(1, 1, 1800);
        System.out.println(sample);
        sample = new Date(31, 12, 2500);
        System.out.println(daysTillFirstOfYear[daysTillFirstOfYear.length - 1]);
        System.out.println(sample.days);
        System.out.println(sample);
        sample = new Date(30, 11, 2012);
        System.out.println(sample);
        sample = sample.increment(100);
        System.out.println(sample);
    }

}
