import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class AdNetwork {
    List<Ad> ads;

    public AdNetwork() {ads = new ArrayList<>();}

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }

    void readAds(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!Objects.equals(line, "")){
            String [] parts = line.split("\\s+");
            StringBuilder tmp = new StringBuilder();
            Arrays.stream(parts).skip(4).forEach(s -> tmp.append(s).append(" "));
            ads.add(new Ad(parts[0],parts[1],Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),tmp.toString()));
            line = br.readLine();
        }
    }

    List placeAds(BufferedReader br, int k, PrintWriter pw) throws IOException {
        String request = br.readLine();
        String [] parts = request.split("\\s+");
        String id = parts[0];
        String category = parts[1];
        Double floorBid = Double.parseDouble(parts[2]);
        String [] keywords = new String[parts.length - 2];
        for (int i = 3; i < parts.length; i++){
            keywords[i - 3] = parts[i];
        }
        StringBuilder keys = new StringBuilder();
        for (int i = 0; i < keywords.length; i++) keys.append(keywords[i]).append(" ");
        AdRequest adRequest = new AdRequest(id,category,floorBid,keys.toString());
        ads.removeIf(s -> s.bidValue < floorBid);
        List<Ad> tmp = ads.stream().sorted(Comparator.comparing((Ad s) -> s.totalScore(relevanceScore(s,adRequest),5.0,s.bidValue,10000.0,s.ctr)).reversed()).collect(Collectors.toList());
        List<Ad> tmps = new ArrayList<>();
        for (int i = 0; i < k; i++){
            tmps.add(tmp.get(i));
        }
        List<Ad> toPrint = tmps.stream().sorted(Comparator.comparing((Ad s) -> s.bidValue).thenComparing((Ad s) -> s.id).reversed()).collect(Collectors.toList());
        pw.println("Top ads for request " + id + ":");
        for (Ad p : toPrint){
            pw.println(p.toString());
        }
        return toPrint;
    }

}


class Ad implements Comparable<Ad>{
    String id;
    String category;
    double bidValue;
    double ctr;
    String content;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    double totalScore(int a,double five, double bidValue,double ten, double ctr){
        return a + five * bidValue + ten * ctr;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    public String getContent() {
        return content;
    }


    //this.id + " " + this.category + " (bid=" + this.bidValue + ", ctr=" + this.ctr + "%) " + this.content
    @Override
    public String toString(){
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s", this.id,this.category,this.bidValue,this.ctr*100,this.content);
    }

    @Override
    public int compareTo(Ad o) {
        if (this == o) return 0;
        int tmp = Double.compare(this.bidValue, o.bidValue);
        if (tmp == 0){
            return this.id.compareTo(o.id);
        }
        return tmp;
    }
}

class AdRequest {
    String id;
    String category;
    double floorBid;
    String keywords;


    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString(){
        return this.id + "[" + this.category + "] (floor=" + this.floorBid + "): " + this.keywords;
    }

}



public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}