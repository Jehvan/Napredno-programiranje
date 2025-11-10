import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде

abstract class Archive {
    private int id;
    private Date date;

    public Archive(int id) {
        this.id = id;
    }

    abstract void openItem(ArchiveStore archiveStore, Date date);

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

class LockedArchive extends Archive {
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    void openItem(ArchiveStore archiveStore, Date date) {
        if (date.before(dateToOpen)) {
            archiveStore.addLog("Item" + getId() + " cannot be opened before " + dateToOpen);
        } else {
            archiveStore.addLog("Item " + getId() + " opened at " + date);
        }
    }

}

class SpecialArchive extends Archive {
    private int id;
    private int maxOpen;
    private int openCount = 0;
    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
    }

    public int getId() {
        return id;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    @Override
    void openItem(ArchiveStore archiveStore, Date date) {
        if (openCount < maxOpen) {
            openCount++;
            archiveStore.addLog("Item" + getId() + " opened at " + date);
        } else {
            archiveStore.addLog("Item " + getId() + " cannot be opened more than " + date + " times");
        }
    }

}

class ArchiveStore {
    List<Archive> archives;
    StringBuilder log = new StringBuilder();
    public ArchiveStore() {
        this.archives = new ArrayList<>();
    }

    void archiveItem(Archive item, Date date) {
        item.setDate(date);
        archives.add(item);
        addLog("Item " + item.getId() + " archived at " + date);
    }

    void openItem(int id, Date date) throws NonExistingItemException {
        for (Archive archive : archives) {
            if(archive.getId() == id) {
                archive.openItem(this, date);
                return;
            }
        }
        throw new NonExistingItemException("Item with " + id + " doesn't exist");
    }

    void addLog(String a){
        log.append(a).append("\n");
    }

    String getLog() {
        return log.toString();
    }
}


class NonExistingItemException extends Exception {
    public NonExistingItemException(String message) {
        super(message);
    }
}
