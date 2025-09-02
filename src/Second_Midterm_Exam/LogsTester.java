package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;

abstract class Log {
    protected String service;
    protected String microservice;
    protected String message;
    protected long timestamp;
    protected int severity;


    public Log(String service, String microservice, String message, long timestamp) {
        this.service = service;
        this.microservice = microservice;

        this.message = message;
        this.timestamp = timestamp;
        this.severity = 0;
    }

    public static Log createLog(String log) {
        String[] parts = log.split("\\s+");

        Log newLog;
        switch (parts[2]) {
            case "INFO":
                newLog = new InfoLog(parts[0], parts[1], Arrays.stream(parts).skip(5).limit(parts.length - 5).collect(Collectors.joining(" ")), Long.parseLong(parts[parts.length - 1]));
                break;
            case "WARN":
                newLog = new WarnLog(parts[0], parts[1], Arrays.stream(parts).skip(5).limit(parts.length - 5).collect(Collectors.joining(" ")), Long.parseLong(parts[parts.length - 1]));
                break;
            case "ERROR":
                newLog = new ErrorLog(parts[0], parts[1], Arrays.stream(parts).skip(5).limit(parts.length - 5).collect(Collectors.joining(" ")), Long.parseLong(parts[parts.length - 1]));
                break;
            default:
                return null;
        }
        return newLog;
    }


    public static Comparator<Log> LogComparator(String order) {
        Comparator<Log> comparator = null;
        switch (order) {
            case "NEWEST_FIRST":
                comparator = Comparator.comparing(Log::getTimestamp).reversed();
                break;
            case "OLDEST_FIRST":
                comparator = Comparator.comparing(Log::getTimestamp);
                break;
            case "MOST_SEVERE_FIRST":
                comparator = Comparator.comparing(Log::getLogSeverity).thenComparing(Log::getTimestamp).thenComparing(Log::getMicroservice).reversed();
                break;
            case "LEAST_SEVERE_FIRST":
                comparator = Comparator.comparing(Log::getLogSeverity);
                break;
        }
        return comparator;
    }

    public String getService() {
        return service;
    }

    public String getMicroservice() {
        return microservice;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }


    abstract public int getLogSeverity();

}

class InfoLog extends Log {

    public InfoLog(String service, String microservice, String message, long timestamp) {
        super(service, microservice, message, timestamp);
        this.severity = 0;
    }

    //service2|microservice2 [INFO] Log message 9. 953 T:953
    @Override
    public String toString() {
        return String.format("%s|%s [INFO] Log message %s T:%d", service, microservice, message, timestamp);
    }

    @Override
    public int getLogSeverity() {
        return severity;
    }
}

class WarnLog extends Log {

    public WarnLog(String service, String microservice, String message, long timestamp) {
        super(service, microservice, message, timestamp);
        String m = message.toLowerCase();
        this.severity = 1 + (m.contains("might cause error") ? 1 : 0);
    }

    @Override
    public String toString() {
        return String.format("%s|%s [WARN] Log message %s T:%d", service, microservice, message, timestamp);
    }

    @Override
    public int getLogSeverity() {
        return severity;
    }
}

class ErrorLog extends Log {

    public ErrorLog(String service, String microservice, String message, long timestamp) {
        super(service, microservice, message, timestamp);
        String m = message.toLowerCase();
        this.severity = 3 + (m.contains("fatal") ? 2 : 0) + (m.contains("exception") ? 3 : 0);
    }

    @Override
    public String toString() {
        return String.format("%s|%s [ERROR] Log message %s T:%d", service, microservice, message, timestamp);
    }

    @Override
    public int getLogSeverity() {
        return severity;
    }
}

class LogCollector {
    private List<Log> logs;
    private Map<String, Map<String, List<Log>>> byServiceAndMicroservice;
    private Map<String, List<Log>> logsByService;

    public LogCollector() {
        this.logs = new ArrayList<>();
        this.byServiceAndMicroservice = new HashMap<>();
        this.logsByService = new HashMap<>();
    }

    public void addLog(String log) {

        Log newLog = Log.createLog(log);
        if (newLog == null) return;

        this.logs.add(newLog);
        this.byServiceAndMicroservice.putIfAbsent(newLog.service, new HashMap<>());
        this.byServiceAndMicroservice.get(newLog.service).putIfAbsent(newLog.microservice, new ArrayList<>());
        this.byServiceAndMicroservice.get(newLog.service).get(newLog.microservice).add(newLog);

        this.logsByService.putIfAbsent(newLog.service, new ArrayList<>());
        this.logsByService.get(newLog.service).add(newLog);

    }

    private int totalLogsPerService(String service) {
        return logsByService.get(service).size();
    }

    private double avgSeverityForAllLogsInService(String service) {
        return logsByService.get(service).stream()
                .mapToDouble(Log::getLogSeverity)
                .average()
                .orElse(0.0);
    }

    private double avgLogsPerMicroservice(String service) {
        int totalLogs = totalLogsPerService(service);
        double numMicroservices = byServiceAndMicroservice.get(service).size();
        return totalLogs / numMicroservices;
    }

    void printServicesBySeverity() {
        byServiceAndMicroservice.keySet().stream()
                .sorted(Comparator.comparing(this::avgSeverityForAllLogsInService).reversed())
                .forEach(service -> {
                    System.out.println(String.format("Service name: %s Count of microservices: %d Total logs in service: %d" +
                                    " Average severity for all logs: %.2f" +
                                    " Average number of logs per microservice: %.2f",
                            service, byServiceAndMicroservice.get(service).size(), totalLogsPerService(service),
                            avgSeverityForAllLogsInService(service), avgLogsPerMicroservice(service)));
                });
    }

    Map<Integer, Integer> getSeverityDistribution(String service, String microservice) {
        Map<Integer, Integer> getSeverityDistribution;

        if (microservice != null) {
            getSeverityDistribution = byServiceAndMicroservice.get(service).get(microservice).stream()
                    .collect(Collectors.groupingBy(
                            l -> l.severity,
                            Collectors.summingInt(i -> 1)
                    ));
        } else {
            getSeverityDistribution = logsByService.get(service).stream()
                    .collect(Collectors.groupingBy(
                            l -> l.severity,
                            Collectors.summingInt(i -> 1)
                    ));
        }
        return getSeverityDistribution;
    }

    void displayLogs(String service, String microservice, String order) {

        if (microservice != null) {
            System.out.printf("displayLogs %s %s %s\n", service, microservice, order);
            byServiceAndMicroservice.get(service).get(microservice)
                    .stream()
                    .sorted(Log.LogComparator(order))
                    .forEach(System.out::println);
        } else {
            System.out.printf("displayLogs %s %s\n", service, order);
            logsByService.get(service)
                    .stream()
                    .sorted(Log.LogComparator(order))
                    .forEach(System.out::println);
        }
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
            } else if (line.startsWith("displayLogs")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}
