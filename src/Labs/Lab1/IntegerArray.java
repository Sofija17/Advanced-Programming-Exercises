package Labs.Lab1;

import java.util.Arrays;

public final class IntegerArray {
    private final int [] integerArray;
    private int copy[];


    public IntegerArray(int[] a) {
        this.integerArray = a;
//        this.copy = Arrays.copyOf(integerArray,integerArray.length);
    }


    public int length (){
            return integerArray.length;
    }

    public int getElement (int i){
        return integerArray[i];
    }

    public int sum (){
        int total=0;
        for (int i=0; i<integerArray.length; i++){
            total += integerArray[i];
        }
        return total;
    }

    public double average (){
        double total = sum();
        return total / integerArray.length;
    }

    public IntegerArray getSorted (){
        IntegerArray copy = new IntegerArray(Arrays.copyOf(integerArray,integerArray.length));
        Arrays.sort(new IntegerArray[]{copy});
        return copy;
    }


}
