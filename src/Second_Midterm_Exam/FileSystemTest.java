package Second_Midterm_Exam;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */

class File implements Comparable<File> {
    private char folder;
    private String name;
    private Integer size;
    private LocalDateTime createdAt;

    public File(char folder, String name, Integer size, LocalDateTime createdAt) {
        this.folder = folder;
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public char getFolder() {
        return folder;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public int compareTo(File o) {
        return Comparator
                .comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparing(File::getSize)
                .compare(this, o);
    }

    //%-10[name] %5[size]B %[createdAt]
    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }


}

class FileSystem {
    private List<File> files;

    public FileSystem() {
        files = new ArrayList<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        files.add(new File(folder, name, size, createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        return files.stream()
                .filter(f -> f.getName().startsWith(".") || f.getFolder() == '.')
                .filter(f -> f.getSize() < size)
                .sorted(Comparator.comparing(File::getFolder).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());

    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
//        List<File> matches = new ArrayList<>();
//        for (int i = 0; i < files.size(); i++) {
//            for (int j = 0; j < folders.size(); j++) {
//                if (files.get(i).getFolder() == folders.get(j)) { //ako najdam match
//                    matches.add(files.get(i));
//                }
//            }
//        }
//        return matches.stream()
//                .mapToInt(File::getSize)
//                .sum();
        Set<Character> folderNames = new HashSet<>(folders);

        return files.stream()
                .filter(f -> folderNames.contains(f.getFolder()))
                .mapToInt(File::getSize)
                .sum();

    }

    public Map<Integer, Set<File>> byYear() {
        //datoteki grupirani spored godina na kreiranje
        Map<Integer, Set<File>> sortedFiles = new HashMap<>();

//        for (File f : files) {
//            int year = f.getCreatedAt().getYear();
//            sortedFiles.putIfAbsent(year, new TreeSet<>());
//            sortedFiles.get(year).add(f);
//        }
//        return sortedFiles;

        return files.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getCreatedAt().getYear(),
                        Collectors.toCollection(TreeSet::new)
                ));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        //APRIL-15 -> 2306
//        Map<String, Long> sizeByMonthAndDay = new HashMap<>();

//        for (File f : files) {
//            Long totalSize = 0L;
//            Object month = f.getCreatedAt().getMonth();
//            int dayOfMonth = f.getCreatedAt().getDayOfMonth();
//            String formatted = month.toString().concat("-").concat(String.valueOf(dayOfMonth));
//
//            for (int i = 0; i < files.size(); i++) {
//                File curr = files.get(i);
//                if (curr.getCreatedAt().getMonth().equals(month) && curr.getCreatedAt().getDayOfMonth() == dayOfMonth) {
//                    totalSize += curr.getSize();
//                }
//            }
//            sizeByMonthAndDay.putIfAbsent(formatted, totalSize);
//        }
//        return sizeByMonthAndDay;
//    }
        return files.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getCreatedAt().getMonth() + "-" + f.getCreatedAt().getDayOfMonth(),
                        HashMap::new,
                        Collectors.summingLong(File::getSize)
                ));
    }
}

    public class FileSystemTest {
        public static void main(String[] args) {
            FileSystem fileSystem = new FileSystem();
            Scanner scanner = new Scanner(System.in);
            int n = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < n; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                fileSystem.addFile(parts[0].charAt(0), parts[1],
                        Integer.parseInt(parts[2]),
                        LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
                );
            }
            int action = scanner.nextInt();
            if (action == 0) {
                scanner.nextLine();
                int size = scanner.nextInt();
                System.out.println("== Find all hidden files with size less then " + size);
                List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
                files.forEach(System.out::println);
            } else if (action == 1) {
                scanner.nextLine();
                String[] parts = scanner.nextLine().split(":");
                System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
                int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                        .map(s -> s.charAt(0))
                        .collect(Collectors.toList()));
                System.out.println(totalSize);
            } else if (action == 2) {
                System.out.println("== Files by year");
                Map<Integer, Set<File>> byYear = fileSystem.byYear();
                byYear.keySet().stream().sorted()
                        .forEach(key -> {
                            System.out.printf("Year: %d\n", key);
                            Set<File> files = byYear.get(key);
                            files.stream()
                                    .sorted()
                                    .forEach(System.out::println);
                        });
            } else if (action == 3) {
                System.out.println("== Size by month and day");
                Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
                byMonthAndDay.keySet().stream().sorted()
                        .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
            }
            scanner.close();
        }
    }

// Your code here

