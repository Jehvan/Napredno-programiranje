import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class SubtitlesTest {
    public static void main(String[] args) throws IOException {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

class Subtitle {
    int num;
    LocalTime start;
    LocalTime end;
    ArrayList<String> text;

    public Subtitle(int num, LocalTime start, LocalTime end, ArrayList<String> text) {
        this.num = num;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    @Override
    public String toString() {
        String st = this.start.format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS"));
        String et = this.end.format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS"));
        StringBuilder s = new StringBuilder(this.num + "\n" + st + " --> " +
                et + "\n");
        for (String a : text) {
            s.append(a).append("\n");
        }
        return s.toString();
    }

}

// Вашиот код овде
class Subtitles {
    ArrayList<Subtitle> subtitles = new ArrayList<>();

    public Subtitles() {
    }

    Subtitle createSubtitle(int num, LocalTime start, LocalTime end, ArrayList<String> text) {
        return new Subtitle(num, start, end, text);
    }


    int loadSubtitles(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int count = 0;
        String line;
        while ((line = br.readLine()) != null && !line.isBlank()) {
            int num = Integer.parseInt(line);
            String time = br.readLine();
            String[] times = time.split(" --> ");
            ArrayList<String> text = new ArrayList<>();
            while ((line = br.readLine()) != null && !line.isBlank()) text.add(line);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm:ss,SSS");
            LocalTime start = LocalTime.parse(times[0], dtf);
            LocalTime end = LocalTime.parse(times[1], dtf);
            count++;
            subtitles.add(createSubtitle(num, start, end, text));
        }
        return count;
    }

    void print() {
        for (Subtitle s : subtitles) {
            System.out.println(s.toString());
        }
    }

    void shift(int ms) {
        subtitles.forEach((Subtitle s) -> s.start = s.start.plus(ms, ChronoUnit.MILLIS));
        subtitles.forEach((Subtitle s) -> s.end = s.end.plus(ms, ChronoUnit.MILLIS));

    }
}