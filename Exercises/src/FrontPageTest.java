import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde

class Category {
    String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }
}


abstract class NewsItem {
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public abstract String getTeaser();

    @Override
    public String toString() {
        return this.getTeaser();
    }
}

class TextNewsItem extends NewsItem {
    String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder();
        Date now = new Date();
        long diff = (now.getTime() - this.date.getTime()) / 1000 / 60 ;
        if (this.text.length() > 80) {
            int parts = this.text.length() / 80;
            teaser.append(this.title).append("\n").append(diff).append("\n");
            String [] substrings = new String[parts];
            for (int i = 0; i < 1; ++i) {
                substrings[i] = this.text.substring(i * 80, (i + 1) * 80);
                teaser.append(substrings[i]);
            }
            return teaser.toString();
        } else {
            teaser.append(this.title).append("\n").append(diff).append("\n").append(this.text);
            return teaser.toString();
        }


    }
}

class MediaNewsItem extends NewsItem {
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder();
        Date now = new Date();
        long diff = (now.getTime() - this.date.getTime()) / 1000 / 60 ;
        teaser.append(this.title).append("\n").append(diff).append("\n").append(this.url).append("\n").append(this.views);
        return teaser.toString();
    }

}


class FrontPage {
    List<NewsItem> newsItems;
    Category[] categories;

    public FrontPage(Category[] categories) {
        this.newsItems = new ArrayList<>();
        this.categories = categories;
    }

    void addNewsItem(NewsItem item) {
        newsItems.add(item);
    }

    List<NewsItem> listByCategory(Category category) {
        return newsItems.stream().filter(item -> category.equals(item.category)).collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String categoryName) throws CategoryNotFoundException {
        for (Category category : categories) {
            if (category.name.equals(categoryName)) {
                return newsItems.stream().filter(item -> category.equals(item.category)).collect(Collectors.toList());
            }
        }
        throw new CategoryNotFoundException("Category " + categoryName + " was not found");
    }

    @Override
    public String toString() {
        StringBuilder teaser = new StringBuilder();
        this.newsItems.forEach(item -> System.out.println(item.toString()));
        return "";
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String categoryName) {
        super(categoryName);
    }
}