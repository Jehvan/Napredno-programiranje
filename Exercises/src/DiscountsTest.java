import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
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

class Store {
    String name;
    Map<Double,Double> prices;
    Map<Double,Double> treeMap;
    double avgDiscount;
    int totalDiscount;
    Comparator<Double> byReduction = (d1, d2) -> {
        Double r1 = prices.get(d1);
        Double r2 = prices.get(d2);
        if (r1 == null || r2 == null)
            throw new IllegalArgumentException("Both keys must be in the map");

        double reduction1 = (r1 - d1) / r1;
        double reduction2 = (r2 - d2) / r2;

        // Compare percentages descending (larger reduction first)
        int cmp = Double.compare(reduction2, reduction1);
        if (cmp != 0)
            return cmp;
        else
            return Double.compare(d1, d2); // tie-breaker by discounted price
    };
    public Store(String name) {
        this.name = name;
        this.prices = new HashMap<>();
        this.treeMap = new TreeMap<>(byReduction);
        this.avgDiscount = 0;
        this.totalDiscount = 0;
    }

    void calculateTotalDiscount(){
        for (Double key : prices.keySet()){
            this.totalDiscount += (int) (prices.get(key) - key);
        }
    }

    void calculateAverageDiscount(){
        for (Double key : prices.keySet()){
            this.avgDiscount += (int)(((prices.get(key) - key)/ prices.get(key)) * 100);
        }
        avgDiscount /= prices.size();
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.name).append("\n")
                .append(String.format("Average discount: %.1f%%", avgDiscount)).append("\n")
                .append("Total discount: ").append(this.totalDiscount).append("\n");
        treeMap.putAll(prices);
        for (Double key : prices.keySet()){
            int popust = (int) ((prices.get(key) - key)/ prices.get(key) * 100);
            s.append(String.format("%2d%% ",popust)).append(String.format("%.0f",key)).append("/").append(String.format("%.0f",prices.get(key))).append("\n");
        }
        return s.toString();
    }

}

class Discounts {
    List<Store> stores = new ArrayList<>();
    int lines = 0;

    void create(String line){
        String[] tokens = line.split("\\s+");
        Store store = new Store(tokens[0]);
        for (int i = 1; i < tokens.length; i++){
            Double discountedPrice = Double.parseDouble(tokens[i].split(":")[0]);
            Double regularPrice = Double.parseDouble(tokens[i].split(":")[1]);
            store.prices.put(discountedPrice,regularPrice);
        }
        store.calculateAverageDiscount();
        store.calculateTotalDiscount();
        stores.add(store);
        lines++;
    }

    public int readStores(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader (inputStream));
        br.lines().forEach(this::create);
        return lines;
    }

    public List<Store> byAverageDiscount(){
        return this.stores.stream().sorted(Comparator.comparing((Store s) -> s.avgDiscount).thenComparing((Store s) -> s.name).reversed()).collect(Collectors.toList()).subList(0,3);
    }

    public List<Store> byTotalDiscount(){
        return this.stores.stream().
                sorted(Comparator.comparing((Store s) -> s.totalDiscount)
                        .thenComparing((Store s) -> s.name))
                .collect(Collectors.toList()).subList(0,3);
    }
}