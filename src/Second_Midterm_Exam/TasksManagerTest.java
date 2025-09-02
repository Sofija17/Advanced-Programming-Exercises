package Second_Midterm_Exam;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(LocalDateTime dateTime) {
        super(String.format("The deadline %s has already passed", dateTime));
    }
}

class Task {
    private String category;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private int priority;

    private final static LocalDate DEADLINE = LocalDate.of(2020, 6, 2);

    public Task(String category, String name, String description, LocalDateTime deadline, int priority) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = null;
        this.priority = 0;
    }


    public static boolean isDateBeforeDeadline(LocalDateTime date) {
        LocalDateTime deadline = DEADLINE.atStartOfDay();

        return date.isBefore(deadline);
    }

    //School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1
    //School,NP,prepare for June exam :)
    //School,NP,lab 2 po NP,2020-06-27T23:59:59.000
    //School,NP,solve all exercises,3
    public static Task createTask(String line) throws DeadlineNotValidException {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            return new Task(parts[0], parts[1], parts[2], LocalDateTime.parse(parts[3]), Integer.parseInt(parts[4]));
        } else if (parts.length == 3) {
            return new Task(parts[0], parts[1], parts[2]);
        } else {
            String part = parts[3];
            if (part.length() > 1) {
                LocalDateTime date = LocalDateTime.parse(part);
                if (Task.isDateBeforeDeadline(date))
                    throw new DeadlineNotValidException(date);
                return new Task(parts[0], parts[1], parts[2], LocalDateTime.parse(part), 0);
            } else {
                return new Task(parts[0], parts[1], parts[2], null, Integer.parseInt(part));
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public long calculateTimeDiff(LocalDateTime time) {
        if(deadline == null) return Long.MAX_VALUE;
        return Duration.between(deadline, time).getSeconds();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Task{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'');
        if(deadline!=null){
            sb.append("deadline= " + deadline + " ");
        }
        if(priority != 0){
            sb.append("priority: " + priority);
        }
        return sb.toString();
    }
}

class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }


    public void readTasks(InputStream in) {
//        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        Scanner sc = new Scanner(in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            try {
                Task task = Task.createTask(line);
                this.tasks.add(task);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
            }
        }
//        this.tasks = bf.lines()
//                .map(line -> {
//                    try{
//                        return Task.createTask(line);
//                    }catch (DeadlineNotValidException e){
//                        System.out.println(e.getMessage());
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
    }

    public void printTasks(PrintStream out, boolean includePriority, boolean includeCategory) {
        PrintWriter pw = new PrintWriter(out);

        Comparator<Task> comparator = Comparator.comparing(Task::getPriority)
                .thenComparing(t -> t.calculateTimeDiff(LocalDateTime.now()));

        if (includeCategory) {
            TreeMap<String, List<Task>> byCategory = tasks.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getCategory(),
                            TreeMap::new,
                            Collectors.toList()
                    ));
            byCategory.forEach((cat,list) -> {
                        pw.println(cat.toUpperCase());
                        list.stream()
                                .forEach(System.out::println);
            });
        }
        if (includePriority) {


            tasks.stream()
                    .sorted(comparator)
                    .forEach(t -> System.out.println(t));
        } else {
            Comparator<Task> comparator2 = Comparator.comparing(t -> t.calculateTimeDiff(LocalDateTime.now()));

            tasks.stream()
                    .sorted(comparator2)
                    .forEach(t -> System.out.println(t));
        }

//        if(includeCategory && includePriority){
//            TreeMap<String, List<Task>> list = tasks.stream()
//                    .collect(Collectors.groupingBy(
//                            t -> t.getCategory(),
//                            TreeMap::new,
//                            Collectors.toCollection(() -> new TreeMap<String, List<Task>>(comparator))
//                    ));
//            for (Map.Entry<String,List<Task>> e : list.entrySet()){
//                System.out.println(e.getKey().toUpperCase());
//                for (Task t : e.getValue()){
//                    System.out.println(e);
//                }
//            }
//        }
        pw.flush();
    }


}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
