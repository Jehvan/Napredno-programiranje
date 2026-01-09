import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Driver {
    String name;
    List<LocalTime> laps;

    public Driver(String name, List<LocalTime> laps) {
        this.name = name;
        this.laps = laps;
    }

    LocalTime getFastestLap(){
        laps = laps.stream().sorted(Comparator.comparing(LocalTime::getHour).thenComparing(Comparator.comparing((LocalTime::getMinute)).thenComparing((LocalTime::getSecond)))).collect(Collectors.toList());
        return laps.get(0);
    }

    @Override
    public String toString(){
        return String.format("%-10s  %-10s", this.name,(getFastestLap().toString()).substring(4,8) + ":" + (getFastestLap().toString()).substring(9));
    }
}

class F1Race {
    List<Driver> drivers = new ArrayList<>();
    // vashiot kod ovde
    void readResults(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader (inputStream));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) break;
            String[] parts = line.split("\\s+");
            parts[1] = "0:" + parts[1];
            parts[2] = "0:" + parts[2];
            parts[3] = "0:" + parts[3];
            List<LocalTime> laps = new ArrayList<>();
            laps.add(LocalTime.parse(parts[1],DateTimeFormatter.ofPattern("H:m:ss:SSS")));
            laps.add(LocalTime.parse(parts[2],DateTimeFormatter.ofPattern("H:m:ss:SSS")));
            laps.add(LocalTime.parse(parts[3],DateTimeFormatter.ofPattern("H:m:ss:SSS")));
            Driver s = new Driver(parts[0], laps);
            drivers.add(s);
        }
    }

    void printSorted(OutputStream outputStream) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter (outputStream));
        List<Driver> tmp = drivers.stream().sorted(Comparator.comparing((Driver s) -> s.getFastestLap())).collect(Collectors.toList());
        for (Driver d : tmp) {
            bw.write((tmp.indexOf(d) + 1) + ". " + d.toString() + "\n");
        }
        bw.flush();
        bw.close();
    }

}