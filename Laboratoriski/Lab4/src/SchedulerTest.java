import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulerTest {


    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        if ( k == 0 ) {
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime()-7200000), jin.next());
            scheduler.add(new Date(now.getTime()-3600000), jin.next());
            scheduler.add(new Date(now.getTime()-14400000), jin.next());
            scheduler.add(new Date(now.getTime()+7200000), jin.next());
            scheduler.add(new Date(now.getTime()+14400000), jin.next());
            scheduler.add(new Date(now.getTime()+3600000), jin.next());
            scheduler.add(new Date(now.getTime()+18000000), jin.next());
            System.out.println(scheduler.getFirst());
            System.out.println(scheduler.getLast());
        }
        if ( k == 3 ) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime()-7200000), jin.next());
            scheduler.add(new Date(now.getTime()-3600000), jin.next());
            scheduler.add(new Date(now.getTime()-14400000), jin.next());
            scheduler.add(new Date(now.getTime()+7200000), jin.next());
            scheduler.add(new Date(now.getTime()+14400000), jin.next());
            scheduler.add(new Date(now.getTime()+3600000), jin.next());
            scheduler.add(new Date(now.getTime()+18000000), jin.next());
            System.out.println(scheduler.next());
            System.out.println(scheduler.last());
            ArrayList<String> res = scheduler.getAll(new Date(now.getTime()-10000000), new Date(now.getTime()+17000000));
            Collections.sort(res);
            for ( String t : res ) {
                System.out.print(t+" , ");
            }
        }
        if ( k == 4 ) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Date> to_remove = new ArrayList<Date>();

            while ( jin.hasNextLong() ) {
                Date d = new Date(jin.nextLong());
                int i = jin.nextInt();
                if ( (counter&7) == 0 ) {
                    to_remove.add(d);
                }
                scheduler.add(d,i);
                ++counter;
            }
            jin.next();

            while ( jin.hasNextLong() ) {
                Date l = new Date(jin.nextLong());
                Date h = new Date(jin.nextLong());
                ArrayList<Integer> res = scheduler.getAll(l,h);
                Collections.sort(res);
                System.out.println(sdf.format(l) +" <: "+print(res)+" >: "+sdf.format(h));
            }
            System.out.println("test");
            ArrayList<Integer> res = scheduler.getAll(new Date(0),new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            for ( Date d : to_remove ) {
                scheduler.remove(d);
            }
            res = scheduler.getAll(new Date(0),new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static <T> String print(ArrayList<T> res) {
        if ( res == null || res.size() == 0 ) return "NONE";
        StringBuffer sb = new StringBuffer();
        for ( T t : res ) {
            sb.append(t+" , ");
        }
        return sb.substring(0, sb.length()-3);
    }


}

class Scheduler<T> {
    Map<Date, T> map = new HashMap<>();

    public Scheduler() {}


    public void add(Date d, T t){
        map.put(d,t);
    }

    public boolean remove(Date d){
        if(map.get(d) != null){
            map.remove(d);
            return true;
        } else return false;
    }

    public T next(){
        Date now = new Date();
        Optional<Date> nextDate = map.keySet().stream()
                .filter(t -> t.after(now))
                .min(Comparator.comparing(Date::getTime));
        return nextDate.map(map::get).orElse(null);
    }

    public T last(){
        Date now = new Date();
        Optional<Date> nextDate = map.keySet().stream()
                .filter(t -> t.before(now))
                .max(Comparator.comparing(Date::getTime));
        return nextDate.map(map::get).orElse(null);
    }

    public T getFirst(){
        Optional<Date> firstDate = map.keySet().stream()
                .min(Comparator.comparing(Date::getTime));
        return firstDate.map(map::get).orElse(null);

    }

    public T getLast(){
        Optional<Date> firstDate = map.keySet().stream()
                .max(Comparator.comparing(Date::getTime));
        return firstDate.map(map::get).orElse(null);
    }

    public ArrayList<T> getAll(Date begin, Date end) {
        ArrayList<T> tmp = new ArrayList<>();
        map.keySet().stream().filter(t -> t.after(begin) && t.before(end))
                .forEach(t -> {
                    tmp.add(map.get(t));
                });
        return tmp;

    }

}