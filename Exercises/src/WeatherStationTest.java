import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;




@SuppressWarnings("deprecated")
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone(""));
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde
class Measurements {
    float temperature;
    float humidity;
    float wind;
    float visibility;
    Date date;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Measurements(float temperature, float humidity, float wind, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    public String formatDateAsGMTString(Date date) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("GMT"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        return zdt.format(formatter);
    }

    @Override
    public String toString() {
        String formattedDate = formatDateAsGMTString(date);
        return temperature + " " + wind + " km/h " + humidity + "% " + visibility + " km " + formattedDate;
    }
}


class WeatherStation {
    int days;
    List<Measurements> measurements;
    int count = 0;

    public WeatherStation(int days) {
        this.days = days;
        measurements = new ArrayList<>();
    }
    @SuppressWarnings("deprecated")
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurements a = new Measurements(temperature,humidity,wind, visibility, date);
        if (measurements.isEmpty()) measurements.add(a);
        else {
            long prev_time = measurements.get(measurements.size() - 1).date.getTime();
            if (date.getTime() - prev_time >= 150000) {
                measurements.add(a);
            }
        }
        long nowTime = date.getTime();
        long threshold = nowTime - (long) days * 24 * 60 * 60 * 1000;
        measurements.removeIf(measurement -> measurement.date.getTime() <= threshold);
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) throws RuntimeException {
        boolean flag = false;
        float avg = 0;

        measurements.sort(Comparator.comparing(date -> date.date));
        for (Measurements measurement : measurements) {
            if ((measurement.date.after(from) && measurement.date.before(to)) || measurement.date.equals(to) || measurement.date.equals(from)) {
                flag = true;
                count++;
                avg += measurement.temperature;
                System.out.println(measurement);
            }
        }
        if (!flag) {
            throw new RuntimeException();
        } else {
            System.out.printf("%s %.2f","Average temperature:",avg/count);
        }
    }


}
