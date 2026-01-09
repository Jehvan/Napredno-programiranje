import java.util.*;
import java.util.stream.Collectors;

class Book implements Comparable<Book> {
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString(){
        return String.format("%s (%s) %.2f",this.title,this.category,this.price);
    }


    @Override
    public int compareTo(Book o) {
        if (Float.compare(this.price,o.price) == 0) {
            return this.title.compareTo(o.title);
        } else return Float.compare(this.price,o.price);
    }
}

class BookCollection {
    List<Book> books;

    public BookCollection() {
        books = new ArrayList<>();
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void printByCategory(String category) {
        List<Book> tmp = books.stream().filter(s -> s.category.equalsIgnoreCase(category)).sorted(Comparator.comparing((Book s) -> s.title).thenComparing(s -> s.price)).collect(Collectors.toList());
        for (Book s : tmp){
            System.out.println(s.toString());
        }
    }

    public List<Book> getCheapestN(int n) {
        List<Book> tmp = books.stream().sorted(Book::compareTo).collect(Collectors.toList());
        List<Book> ns = new ArrayList<>();
        for (Book b : tmp){
            if (b != null){
                ns.add(b);
            }
        }
        return ns;
    }
}


public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде