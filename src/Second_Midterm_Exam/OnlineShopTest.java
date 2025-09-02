package Second_Midterm_Exam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String id) {
        super(String.format("Product with id %s does not exist in the online shop!", id));
    }
}

class Product {

    private String category;
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;
    private int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = 0;
    }
    public static Comparator<Product> getComparator(COMPARATOR_TYPE comparatorType) {
        Comparator<Product> comparator = null;

        switch (comparatorType) {
            case NEWEST_FIRST:
                comparator = Comparator.comparing(Product::getCreatedAt).reversed();
                break;
            case OLDEST_FIRST:
                comparator = Comparator.comparing(Product::getCreatedAt);
                break;
            case LOWEST_PRICE_FIRST:
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case HIGHEST_PRICE_FIRST:
                comparator = Comparator.comparing(Product::getPrice).reversed();
                break;
            case MOST_SOLD_FIRST:
                comparator =Comparator .comparing(Product::getQuantitySold).reversed().thenComparing(Product::getCreatedAt);
                break;
            case LEAST_SOLD_FIRST:
                comparator = Comparator.comparing(Product::getQuantitySold);
                break;
        }
        return comparator;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {
    private List<Product> products;
    private Map<String, Product> productMap;

    OnlineShop() {
        this.products = new ArrayList<>();
        this.productMap = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product p = new Product(category, id, name, createdAt, price);
        this.products.add(p);
        this.productMap.putIfAbsent(id, p);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!productMap.containsKey(id))
            throw new ProductNotFoundException(id);

        productMap.get(id).setQuantitySold(quantity);
        return productMap.get(id).getPrice() * quantity;
    }


    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        Comparator<Product> comparator = Product.getComparator(comparatorType);

        List<Product> byCategory = products.stream()
                .filter(p -> category==null || p.getCategory().equals(category))
                .sorted(comparator)
                .collect(Collectors.toList());

        List<List<Product>> result = new ArrayList<>();
        List<Product> currentPage = new ArrayList<>();

        for (Product p : byCategory){
            currentPage.add(p);
            if (currentPage.size() == pageSize){
                result.add(currentPage);
                currentPage = new ArrayList<>();
            }
        }

        if(!currentPage.isEmpty())
            result.add(currentPage);

        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


