import java.io.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}


class TimeTable {
    List<LocalTime> times = new  ArrayList<>();

    public TimeTable() {
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        InputStreamReader isr = new InputStreamReader(inputStream);
        Scanner in = new Scanner(isr);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] parts = line.split(" ");
            if (line.isEmpty()) break;
            try {
                for (int i = 0; i < parts.length; i++) {
                    String hour;
                    String minute;
                    if (parts[i].contains(":") || parts[i].contains(".")) {
                        if (!parts[i].contains(":")) {
                            parts[i] = parts[i].replace(".", ":");
                        }
                        hour = parts[i].split(":")[0];
                        minute = parts[i].split(":")[1];
                        invalidTime(parts, i, hour, minute);
                        times.add(LocalTime.parse(parts[i], DateTimeFormatter.ofPattern("H:mm")));
                    } else {
                        throw new UnsupportedFormatException(parts[i]);
                    }
                }
            } catch (UnsupportedFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void writeTimes(OutputStream outputStream, TimeFormat timeFormat) {
        PrintWriter osw = new PrintWriter(outputStream);
        times.sort(Comparator.comparing(LocalTime::toString));

        for (LocalTime time : times) {
            if (timeFormat == TimeFormat.FORMAT_24) {
                osw.println(String.format("%2d:%02d",time.getHour(), time.getMinute()));
            } else {
                osw.println(formatAMPM(time));
            }
        }
        osw.flush();

    }

    private String formatAMPM(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        int displayHour;
        String period;

        if (hour == 0) {
            displayHour = 12;
            period = "AM";
        } else if (hour < 12) {
            displayHour = hour;
            period = "AM";
        } else if (hour == 12) {
            displayHour = 12;
            period = "PM";
        } else {
            displayHour = hour - 12;
            period = "PM";
        }

        return String.format("%2d:%02d %s", displayHour, minute, period);
    }
    private void invalidTime(String[] parts, int i, String hour, String minute) {
        try {
            if (Integer.parseInt(hour) < 0 || Integer.parseInt(hour) > 23 || Integer.parseInt(minute) < 0 || Integer.parseInt(minute) > 59) {
                throw new InvalidTimeException(parts[i]);
            }
        } catch (InvalidTimeException e) {
            System.out.println(e.getMessage());
        }
    }
}








class UnsupportedFormatException extends Exception {
    String part;
    public UnsupportedFormatException(String part) {
        this.part = part;
    }

    public String getMessage(){
        return "UnsupportedFormatException: " + part;
    }
}

class InvalidTimeException extends Exception {
    String message;
    public InvalidTimeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "InvalidTimeException: " + message;
    }

}
