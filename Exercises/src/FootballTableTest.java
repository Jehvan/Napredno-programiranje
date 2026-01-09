import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here
class Game {
    String homeTeam;
    String awayTeam;
    int homeGoals;
    int awayGoals;

    public Game(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }


}


class FootballTable {
    Map<String, List<Game>> map = new HashMap<>();

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        Game g = new Game(homeTeam,awayTeam,homeGoals,awayGoals);
        map.computeIfAbsent(homeTeam, k -> new ArrayList<>()).add(g);
        map.computeIfAbsent(awayTeam, k -> new ArrayList<>()).add(g);
    }

    public void printTable(){
        
    }
}
