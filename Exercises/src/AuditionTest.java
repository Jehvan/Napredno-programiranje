import java.util.*;
import java.util.stream.Collectors;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == "") break;
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant implements Comparator<Participant> {
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return code + " " + name + " " + age;
    }


    @Override
    public int compare(Participant o1, Participant o2) {
        if (Objects.equals(o1.code, o2.code)) {
            return 0;
        } else {
            return -1;
        }
    }
}


class Audition {
    HashMap<String, List<Participant>> map = new HashMap<>();
    HashMap<String, String> codes = new HashMap<>();

    void addParticpant(String city, String code, String name, int age) {
        Participant p = new Participant(code, name, age);
        if (!map.containsKey(city)) map.computeIfAbsent(city, k-> new ArrayList<>()).add(p);
        List<Participant> tmp = map.get(city);
        boolean flag = false;
        for (Participant part : tmp) {
            if (part.code.equals(p.code)) flag = true;
        }
        if (!flag) map.computeIfAbsent(city, k -> new ArrayList<>()).add(p);
    }

    void listByCity(String city) {
        List<Participant> tmp = new ArrayList<>();
        tmp = map.get(city).stream().sorted(Comparator.comparing((Participant p) -> p.name).thenComparing((Participant p) -> p.age).thenComparing((Participant p) -> p.code)).collect(Collectors.toList());
        for (Participant p : tmp) {
            System.out.println(p.toString());
        }
        ;
    }
}