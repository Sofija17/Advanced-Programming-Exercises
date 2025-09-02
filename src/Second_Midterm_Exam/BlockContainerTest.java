package Second_Midterm_Exam;
import java.util.*;
import java.util.stream.Collectors;


class Block<T extends Comparable<T>>{
    private Set<T> elements;
    private int blockSize;

    public Block(int n) {
        this.elements = new TreeSet<>();
        this.blockSize = n;
    }

    public void addElement (T elem){
        this.elements.add(elem);
    }
    public boolean removeElement (T elem){
        return this.elements.remove(elem);
    }

    public Set<T> getElements() {
        return elements;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public String toString() {
        return "[" + elements.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                +"]";
    }
}

class BlockContainer<T extends Comparable<T>>{
    private int maxBlockElements;
    private ArrayList<Block<T>> blocks;


    public BlockContainer(int maxBlockElements) {
        this.maxBlockElements = maxBlockElements;
        this.blocks = new ArrayList<>();
    }

    public void add(T a){

      ;
        if (blocks.isEmpty() ||
                blocks.get(blocks.size()-1).getElements().size() == maxBlockElements){
            Block<T> newBlock = new Block<>(maxBlockElements);
            newBlock.addElement(a);
            this.blocks.add(newBlock);
            return;
        }
        Block<T> last = blocks.get(blocks.size()-1);
        last.addElement(a);
    }
    public boolean remove(T a){
        Block<T> last = blocks.get(blocks.size()-1);
        boolean elem = last.removeElement(a);

        if (last.getElements().isEmpty())
            this.blocks.remove(last);

        return elem;
    }

    public void sort(){
        List<T> allElements = blocks.stream()
                .flatMap(block -> block.getElements().stream())
                .sorted()
                .collect(Collectors.toList());

        blocks.clear();
        for (T elem : allElements){
            add(elem);
        }

    }
    public String toString(){

        return blocks.stream()
                .map(Block::toString)
                .collect(Collectors.joining(","));
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



