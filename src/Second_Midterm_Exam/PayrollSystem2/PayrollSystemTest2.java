package Second_Midterm_Exam.PayrollSystem2;

import java.util.*;


abstract class Employee {
    protected String id;
    protected String level;
    protected String bonus;


    public static final Comparator<Employee> COMPARATOR = Comparator
            .comparing(Employee::getBaseSalary)
            .reversed()
            .thenComparing(Employee::getLevel);


    double hourlyRate = 0;
    double ticketRate = 0;

    public Employee(String id, String level, String bonus) {
        this.id = id;
        this.level = level;
        this.bonus = bonus;
    }

    public String getLevel() {
        return level;
    }

    public double checkBonus() {
        double tmp = 0.0;

        if (bonus.endsWith("%")){
            double parseBonus = Double.parseDouble(bonus.substring(0,bonus.length()-1));
            tmp = (parseBonus / 100.0) * getBaseSalary();
            this.bonus = String.valueOf(tmp);
            return tmp;
        }
        tmp = Double.parseDouble(bonus);
        this.bonus = String.valueOf(tmp);

        return tmp;
    }

    abstract double getBaseSalary();

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f" , id, level, getBaseSalary() + checkBonus());
    }
}

class HourlyEmployee extends Employee {

    private double hours;
    private double hourlyRateByLevel;
    private double regularHours, overtimeHours;

    public HourlyEmployee(String id, String level, double hours, Double hourlyRateByLevel, String bonus) {
        super(id, level, bonus);
        this.hours = hours;
        this.hourlyRateByLevel = hourlyRateByLevel;
        if (hours <= 40) {
            this.regularHours = hours;
            this.overtimeHours = 0;
        } else {
            this.regularHours = 40;
            this.overtimeHours = hours - 40;
        }
    }


    @Override
    double getBaseSalary() {
        return (regularHours * hourlyRateByLevel + overtimeHours * hourlyRateByLevel * 1.5);
    }

    @Override
    public String toString() {
        //Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f Bonus: %.2f ", regularHours, overtimeHours, Double.parseDouble(bonus));
    }
}

class FreelanceEmployee extends Employee {
    private List<Integer> tickets;
    private double ticketRateByLevel;


    public FreelanceEmployee(String id, String level, List<Integer> tickets, double ticketRateByLevel, String bonus) {
        super(id, level, bonus);
        this.tickets = tickets;
        this.ticketRateByLevel = ticketRateByLevel;
    }


    @Override
    double getBaseSalary() {
        return ticketRateByLevel * ticketPoints();
    }

    private int ticketPoints() {
        return tickets.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public String toString() {
        //Employee ID: 596ed2 Level: level10 Salary: 1290.00 Tickets count: 9 Tickets points: 43
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d Bonus: %.2f", tickets.size(), ticketPoints(),  Double.parseDouble(bonus));
    }
}

class BonusNotAllowedException extends Exception {
    public BonusNotAllowedException(String bonus) {

        super(bonus.contains("%")
                ? String.format("Bonus of %s is not allowed", bonus)
                : String.format("Bonus of %s$ is not allowed", bonus));
    }
}

class PayrollSystem {
    private List<Employee> employees;
    private Map<String, Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;


    PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }


    //H;Stefan;level1;45.0 10%
    //F;Gjorgji;level2;1;5;10;6
    public Employee createEmployee(String line) throws BonusNotAllowedException {

        this.employees = new ArrayList<>();
        String id, level, bonus;
        double hours = 0.0;
        Employee e = null;


        String[] parts = line.split(";");

        id = parts[1];
        level = parts[2];
        String[] b = parts[parts.length-1].split("\\s+");

        if(b.length>1){
           bonus = b[1];

           if (bonus.contains("%") && Double.parseDouble(bonus.substring(0, bonus.length() - 1)) > 20)
                throw new BonusNotAllowedException(bonus);
            if(Double.parseDouble(bonus.substring(0, bonus.length() - 1)) > 1000)
                throw new BonusNotAllowedException(bonus);
        } else {
            bonus = "0";
        }


        if (parts[0].equals("F")) {
            List<Integer> tickets = new ArrayList<>();
            for (int i = 3; i < parts.length-1 ; i++) {
                tickets.add(Integer.valueOf(parts[i]));
            }
            e = new FreelanceEmployee(id, level, tickets, ticketRateByLevel.get(level), bonus);

        } else {
            hours = Double.parseDouble(b[0]);
            e = new HourlyEmployee(id, level, hours, hourlyRateByLevel.get(level), bonus);

        }
        employees.add(e);
        return e;
    }
}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
//            case 2: //Testing getOvertimeSalaryForLevels()
//                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
//                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
//                });
//                break;
//            case 3: //Testing printStatisticsForOvertimeSalary()
//                ps.printStatisticsForOvertimeSalary();
//                break;
//            case 4: //Testing ticketsDoneByLevel
//                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
//                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
//                });
//                break;
//            case 5: //Testing getFirstNEmployeesByBonus (int n)
//                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
//                break;
        }

    }
}
