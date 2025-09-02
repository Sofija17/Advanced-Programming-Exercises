package Second_Midterm_Exam.PayrollSystem1;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee {
    protected String id;
    protected String level;

    public static final Comparator<Employee> COMPARATOR = Comparator
            .comparing(Employee::getSalary)
            .reversed()
            .thenComparing(Employee::getLevel)
            .thenComparing(e -> e.id);

    double hourlyRate = 0;
    double ticketRate = 0;

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    abstract double getSalary();

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f", id, level, getSalary());
    }
}

class HourlyEmployee extends Employee {

    private double hours;
    private double hourlyRateByLevel;
    private double regularHours, overtimeHours;

    public HourlyEmployee(String id, String level, double hours, Double hourlyRateByLevel) {
        super(id, level);
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
    double getSalary() {
        return (regularHours * hourlyRateByLevel + overtimeHours * hourlyRateByLevel * 1.500);
    }

    @Override
    public String toString() {
        //Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", regularHours, overtimeHours);
    }
}

class FreelanceEmployee extends Employee {
    private List<Integer> tickets;
    private double ticketRateByLevel;

    public FreelanceEmployee(String id, String level, List<Integer> tickets, double ticketRateByLevel) {
        super(id, level);
        this.tickets = tickets;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    @Override
    double getSalary() {
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
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d", tickets.size(), ticketPoints());
    }
}

class PayrollSystem {
    private List<Employee> employees;
    private Map<String, Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;


    public void readEmployees(InputStream is) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        this.employees = new ArrayList<>();
        List<String> lines = bf.lines().collect(Collectors.toList());

        String id, level;

        double hours = 0.0;

        for (String line : lines) {

            String[] parts = line.split(";");

            id = parts[1];
            level = parts[2];

            if (parts[0].equals("F")) {
                List<Integer> tickets = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    tickets.add(Integer.valueOf(parts[i]));
                }
                employees.add(new FreelanceEmployee(id, level, tickets, ticketRateByLevel.get(level)));
            } else {
                hours = Double.parseDouble(parts[3]);
                employees.add(new HourlyEmployee(id, level, hours, hourlyRateByLevel.get(level)));

            }

        }
    }

    PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }


    public Map<String, Set<Employee>> printEmployeesByLevels(PrintStream out, Set<String> levels) {
        Map<String, Set<Employee>> levelToEmployees = new LinkedHashMap<>();
        levels.forEach(l -> {
            employees.stream()
                    .filter(e -> e.getLevel().equals(l))
                    .forEach(e ->
                    {
                        levelToEmployees.putIfAbsent(l, new TreeSet<>(Employee.COMPARATOR));
                        levelToEmployees.get(l).add(e);
                    });
        });
        return levelToEmployees;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}

