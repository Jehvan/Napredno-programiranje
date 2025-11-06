import java.util.*;
import java.util.stream.Collectors;

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection c) {
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String [] split =  input.nextLine().split("\\s+");
            if (split.length == 1) {
                return split[0];
            }
            c.addCar(new Car(split[0],split[1],Integer.parseInt(split[2]),Float.parseFloat(split[3])));
        }
        input.close();
        return "";
    }
}


// vashiot kod ovde

class Car {
    private String manufacturer;
    private String model;
    private int price;
    private float power;
    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }

    @Override
    public String toString() {
        return manufacturer + " " + model + " (" + (int)power + "KW) " + price;
    }

}

class CarCollection {
    private List<Car> carList;

    public CarCollection() {
        carList = new ArrayList<>();
    }

    public void addCar(Car car) {
        carList.add(car);
    }

    public void sortByPrice(boolean asc) {
        ArrayList<Car> temp = new ArrayList<>();
        if (asc) {
            Collections.sort(this.carList, Comparator.comparing(Car::getPrice).thenComparing(Car::getPower));
        } else {
            Collections.sort(this.carList, Comparator.comparing(Car::getPrice).thenComparing(Car::getPower).reversed());
        }
    }

    public List<Car> filterByManufacturer(String manufacturer) {
//        List<Car> carList = new ArrayList<>();
//        for (Car c : carList) {
//            if (c != null) {
//                if (c.getManufacturer().equals(manufacturer)) {
//                    carList.add(c);
//                }
//            }
//        }
//        return carList;
//        this.sortByPrice(true);

        return this.carList.stream().sorted(Comparator.comparing(Car::getModel)).filter(c -> c.getManufacturer().equalsIgnoreCase(manufacturer)).collect(Collectors.toList());
    }

    public List<Car> getList() {
        return carList;
    }

    @Override
    public String toString() {
        return carList.toString();
    }
}