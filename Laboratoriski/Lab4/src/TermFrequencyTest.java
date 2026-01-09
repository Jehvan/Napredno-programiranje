import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.io.*;
import java.util.*;


public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
class TermFrequency {
    Map<String, Integer> map = new TreeMap<>();

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Set<String> stopSet = new HashSet<>();
        for (String sw : stopWords) stopSet.add(sw.toLowerCase());

        br.lines().forEach(line -> {
            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                String word = token.toLowerCase();
                while (word.startsWith(",") || word.startsWith(".")) word = word.substring(1);
                while (word.endsWith(",") || word.endsWith(".")) word = word.substring(0, word.length() - 1);
                if (word.isBlank()) continue;
                if (stopSet.contains(word)) continue;
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        });
    }

    int countTotal() {
        int total = 0;
        for (int count : map.values()) total += count;
        return total;
    }

    int countDistinct() {
        return map.size();
    }

    List<String> mostOften(int k) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(
                Comparator.comparing(Map.Entry<String, Integer>::getValue, Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey)
        );
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, entries.size()); i++) {
            result.add(entries.get(i).getKey());
        }
        return result;
    }
}
