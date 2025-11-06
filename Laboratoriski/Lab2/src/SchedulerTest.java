import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SchedulerTest {

    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
        if (k == 5) {
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            String description = "hello";
            Function<String, String> f = c -> "bitch";
            Predicate<String> match = c -> c.equals(description);
            Scheduler<String> res =  scheduler.map(scheduler,f,match);
            System.out.println(res);
        }
        if (k == 6) {
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            String description = "hello";
            Predicate<String> match = c -> c.equals(description);
            long res =  scheduler.countIf(scheduler, match);
            System.out.println(res);
        }

        if (k == 7) {
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            Scheduler<String> scheduler2 = new Scheduler<>();
            scheduler2.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler2.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler2.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler2.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler2.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler2.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler2.add(new Timestamp<>(now.plusHours(5), jin.next()));
            Scheduler<String> scheduler3 = new Scheduler<>();
            scheduler3 = scheduler3.merge(scheduler,scheduler2);
            System.out.println(scheduler3);
        }
    }


    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }

}

// vashiot kod ovde

class Timestamp<T> implements Comparable<Timestamp<T>> {
    final LocalDateTime time;
    final T element;

    public Timestamp(LocalDateTime time, T element) {
        this.time = time;
        this.element = element;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public T getElement() {
        return element;
    }

    @Override
    public int compareTo(Timestamp<T> t) {
        return time.compareTo(t.time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timestamp<?> that = (Timestamp<?>) o;
        return time.equals(that.time);
    }

    @Override
    public String toString() {
        return time.toString() + " " + element.toString();
    }

}

class Scheduler<T> {
    Timestamp<T>[] timestamps;
    int size = 0;

    @SuppressWarnings("unchecked")
    public Scheduler() {
        this.timestamps = new Timestamp[10];
    }

    @SuppressWarnings("unchecked")
    public void add(Timestamp<T> t) {
        if (size == timestamps.length) {
            Timestamp<T>[] temp = new Timestamp[timestamps.length * 2];
            System.arraycopy(timestamps, 0, temp, 0, size);
            timestamps = temp;
        }
        timestamps[size++] = t;
    }

    public boolean remove(Timestamp<T> t) {
        for (int i = 0; i < size; i++) {
            if (t.equals(timestamps[i])) {
                timestamps[i] = null;
                return true;
            }
        }
        return false;
    }

    public Timestamp<T> next() {
        Timestamp<T> next = new Timestamp<>(null, null);
        int max = -1000000;
        boolean flag = true;
        for (int i = 0; i < size; i++) {
            if (timestamps[i] != null) {
                if (timestamps[i].time.isAfter(LocalDateTime.now())) {
                    if (flag) {
                        flag = false;
                        next = timestamps[i];
                    }
                    if (next.compareTo(timestamps[i]) > 0) {
                        next = timestamps[i];
                    }
                }
            }
        }
        return next;
    }


    public Timestamp<T> last() {
        Timestamp<T> last = new Timestamp<>(null, null);
        int max = -1000000;
        boolean flag = true;
        for (int i = 0; i < size; i++) {
            if (timestamps[i] != null) {
                if (timestamps[i].time.isBefore(LocalDateTime.now())) {
                    if (flag) {
                        flag = false;
                        last = timestamps[i];
                    }
                    if (last.compareTo(timestamps[i]) < 0) {
                        last = timestamps[i];
                    }
                }
            }
        }
        return last;
    }

    public List<Timestamp<T>> getAll(LocalDateTime start, LocalDateTime end) {
        List<Timestamp<T>> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (timestamps[i] != null) {
                if (timestamps[i].time.isAfter(start) && timestamps[i].time.isBefore(end)) {
                    res.add(timestamps[i]);
                }
            }
        }
        return res;
    }

    public Scheduler<T> map(Scheduler<? extends T> source, Function<? super T, ? extends T> mapper, Predicate<? super T> filter) {
        Scheduler<T> temp =  new Scheduler<>();
        for (int i = 0; i < size; i++) {
            if (source.timestamps[i] != null && filter.test(source.timestamps[i].element)) {
                T mappedElement = mapper.apply(source.timestamps[i].element);
                Timestamp<T> a = new Timestamp<>(LocalDateTime.now(), mappedElement);
                temp.add(a);
            }
        }
        return temp;
    }

    public long countIf(Scheduler<? extends T> source, Predicate<? super T> filter) {
        long count = 0;
        for (int i = 0; i < size; i++) {
            if (source.timestamps[i] != null && filter.test(source.timestamps[i].element)) {
                count++;
            }
        }
        return count;
    }

    public Scheduler<T> merge(Scheduler<T> source, Scheduler<T> source2) {
        Scheduler<T> temp = new Scheduler<>();
        for (int i = 0; i < source.size; i++) {
            temp.add(source.timestamps[i]);
        }
        for (int i = 0; i < source2.size; i++) {
            temp.add(source2.timestamps[i]);
        }
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (timestamps[i] != null) {
                sb.append(timestamps[i].toString()).append(" ");
            }
        }
        return sb.toString();
    }
}