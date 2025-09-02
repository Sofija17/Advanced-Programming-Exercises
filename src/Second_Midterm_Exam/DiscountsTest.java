package Second_Midterm_Exam;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */


class Store {
    private String name;
    private List<Integer> originalPrice;
    private List<Integer> salePrice;

    public Store(String name, List<Integer> originalPrice, List<Integer> salePrice) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    //Desigual 5967:9115  5519:9378  3978:5563  7319:13092  8558:10541
    public static Store createStore(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];

        List<Integer> originalPrice = new ArrayList<>();
        List<Integer> salePrice = new ArrayList<>();


        for (int i = 1; i < parts.length; i++) {
            //5967:9115
            String[] pricePair = parts[i].split(":");
            salePrice.add(Integer.valueOf(pricePair[0]));
            originalPrice.add(Integer.valueOf(pricePair[1]));

        }
        return new Store(name, originalPrice, salePrice);
    }

    public int calculateTotalDiscount() {
        int sum = 0;
        for (int i = 0; i < originalPrice.size(); i++) {
            sum += originalPrice.get(i) - salePrice.get(i);
        }

        return sum;
    }

    public int calculateSalePercent(int oldP, int newP) {
        return ((oldP - newP) * 100) / oldP;
    }


    public double calculateAverageDiscountPerStore() {
        double total = 0.0;
        for (int i = 0; i < originalPrice.size(); i++) {
            total += calculateSalePercent(originalPrice.get(i), salePrice.get(i));
        }
        return total / originalPrice.size();
    }

    private List<Integer> sortedProductDiscounts() {
        List<Integer> discounts = new ArrayList<>();
        int disc = 0;
        for (int i = 0; i < originalPrice.size(); i++) {
            disc = calculateSalePercent(originalPrice.get(i), salePrice.get(i));
            discounts.add(disc);
        }

        return discounts.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public Map<Integer, String> sortProductDiscountByPercent() {
        List<String> prices = new ArrayList<>();
        Map<Integer, String> mapa = new TreeMap<>(Comparator.reverseOrder());
        int discountPercent = 0;
        for (int i = 0; i < originalPrice.size(); i++) {
            String a = String.valueOf(salePrice.get(i)).concat("/").concat(String.valueOf(originalPrice.get(i)));
            discountPercent = calculateSalePercent(originalPrice.get(i), salePrice.get(i));

            mapa.put(discountPercent, a);

        }
        return mapa;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append("\n");
        sb.append(String.format("Average discount: %.1f%% \n", calculateAverageDiscountPerStore()));
        sb.append("Total discount: ").append(calculateTotalDiscount()).append("\n");
//        for (int i=0; i<originalPrice.size(); i++){
//            sb.append(calculateSalePercent(originalPrice.get(i),salePrice.get(i))).append("% ");
//            sb.append(salePrice.get(i)).append("/").append(originalPrice.get(i)).append("\n");
//        }
        Map<Integer, String> mapa = sortProductDiscountByPercent();

        String products = mapa.entrySet()
                .stream()
                .map(e -> e.getKey() + "% " + e.getValue())
                .collect(Collectors.joining("\n"));

        sb.append(products);

//        for (Map.Entry<Integer,String> entry : mapa.entrySet()){
//            sb.append(entry.getKey()).append("% ");
//            sb.append(entry.getValue()).append("\n");
//        }
        return sb.toString();
    }


}

class Discounts {
    private List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }


    public int readStores(InputStream inputStream) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        List<Store> stores = bf.lines()
                .map(Store::createStore)
                .collect(Collectors.toList());

        this.stores = stores;

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        Comparator<Store> comparator = Comparator.comparing(Store::calculateAverageDiscountPerStore)
                .thenComparing(Store::getName);

        return stores.stream()
                .sorted(comparator.reversed())
                .limit(3)
                .collect(Collectors.toList());

    }


    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::calculateTotalDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());

    }
}


public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde