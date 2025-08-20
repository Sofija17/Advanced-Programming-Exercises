
package First_Midterm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class FileNameExistsException extends Exception {

    public FileNameExistsException(String fileName, String folderName) {
        super(String.format("There is already a file named %s in the folder %s", fileName, folderName));
    }
}

interface IFile extends Comparable<IFile> {
    String getFileName();

    long getFileSize();

    String getFileInfo(int indent);

    void sortBySize();

    Long findLargestFile();

    default int compareTo(IFile other) {
        return Long.compare(this.getFileSize(), other.getFileSize());
    }

}

class File implements IFile {
    private String fileName;
    private long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("     ");
        }
        sb.append(String.format("File name: %10s File size: %10d\n", getFileName(), getFileSize()));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public Long findLargestFile() {
        return this.fileSize;
    }

}

class Folder implements IFile {
    private String folderName;
    private long fileSize;
    private List<IFile> files;

    public Folder(String folderName) {
        this.folderName = folderName;
        this.fileSize = 0;
        this.files = new ArrayList<>();
    }


    void addFile(IFile file) throws FileNameExistsException {

        Optional<IFile> res = files.stream()
                .filter(f -> f.getFileName().equals(file.getFileName()))
                .findAny();

        if(res.isPresent())
            throw new FileNameExistsException(file.getFileName(), folderName);

        files.add(file);
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String getFileName() {
        return folderName;
    }

    @Override
    public long getFileSize() {
        return files.stream()
                .mapToLong(file -> file.getFileSize())
                .sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<indent; i++){
            sb.append("     ");
        }
        sb.append(String.format("Folder name: %10s Folder size: %10d \n", getFileName(), getFileSize()));

        files.stream()
                .forEach(f -> sb.append(f.getFileInfo(indent + 1)));

        return sb.toString();
    }

    @Override
    public void sortBySize() {
        files.stream()
                .filter(f -> f instanceof Folder)
                .forEach(f -> ((Folder) f).sortBySize());

        files = files.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

    }

    @Override
    public Long findLargestFile() {
        OptionalLong largest = files.stream().mapToLong(IFile::findLargestFile).max();
        if (largest.isPresent())
            return largest.getAsLong();
        else return 0L;
    }

}

class FileSystem {
    private Folder rootDir;

    public FileSystem() {
        this.rootDir = new Folder("root");
    }

    void addFile(IFile file) throws FileNameExistsException {
        rootDir.addFile(file);
    }

    long findLargestFile() {
        return rootDir.findLargestFile();
    }

    void sortBySize() {
        rootDir.sortBySize();
    }

    @Override
    public String toString() {
        return rootDir.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}