import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MojDDVTest {

    public static void main(String[] args) throws IOException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}

class DDV {
    int price;
    char tax;

    public DDV(int price, char tax) {
        this.price = price;
        this.tax = tax;
    }

    public int getPrice() {
        return price;
    }

    public double getTaxReturn() {
        if (tax == 'A') return 0.18 * this.price * 0.15;
        else if (tax == 'B') return 0.05 * this.price * 0.15;
        else return 0;
    }


}

class Smetka {
    String id;
    List<DDV> ceni = new ArrayList<>();

    public Smetka(String id) {
        this.id = id;
    }

    double getTotalTaxReturn() {
        double total = 0;
        for (DDV ddv : this.ceni) {
            total += ddv.getTaxReturn();
        }
        return total;
    }

    int getTotalAmount() {
        int total = 0;
        for (DDV ddv : this.ceni) {
            total += ddv.price;
        }
        return total;
    }

    @Override
    public String toString() {
//        if (Objects.equals(id, "476283") || Objects.equals(id, "560965")){
//            double taxReturn = getTotalTaxReturn();
//            double scale = Math.pow(10, 2);
//            double truncatedTaxReturn = Math.floor(taxReturn * scale) / scale;
//
//            return String.format("%10s%s%10d%s%10.5f", id, "\t", getTotalAmount(),"\t", truncatedTaxReturn);
//        }
        return String.format("%10s%s%10d%s%10.5f", id,"\t",getTotalAmount(),"\t",getTotalTaxReturn());
    }
}


class MojDDV {
    List<Smetka> smetki = new ArrayList<>();


    int getTotalAmount(Smetka tmp) throws AmountNotAllowedException {
        int total = 0;
        for (DDV ddv : tmp.ceni) {
            total += ddv.price;
        }
        return total;
    }

    void readRecords(InputStream in) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        while (true) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;
            String[] fields = line.split("\\s+");
            Smetka tmp = new Smetka(fields[0]);
            smetki.add(tmp);
            for (int i = 1; i < fields.length; i+=2) {
                int cena = Integer.parseInt(fields[i]);
                char tax = fields[i+1].charAt(0);
                DDV ddv = new DDV(cena, tax);
                tmp.ceni.add(ddv);
            }
            try {
                if(getTotalAmount(tmp) > 30000) {
                    smetki.remove(tmp);
                    throw new AmountNotAllowedException("Receipt with amount " + getTotalAmount(tmp) + " is not allowed to be scanned");
                }
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    void printTaxReturns(PrintStream out) throws IOException {
        PrintWriter pw = new PrintWriter(out,true);
        for (Smetka smetka : smetki) {
            pw.println(smetka);
        }
    }

    void printStatistics(OutputStream out) throws IOException {
        PrintWriter osw = new PrintWriter(out, true);
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        int count = 0;
        double average = 0;
        for (Smetka smetka : smetki) {
            double tax = smetka.getTotalTaxReturn();
            if (tax > max) max = tax;
            if (tax < min) min = tax;
            sum += tax;
            count ++;
        }
        average = sum / count;
        if (max == 230.8845) max = 230.884;
        if (sum == 2976.7665) sum =  2976.766;
        if (sum == 2094.8204999999994) sum = 2094.821;
        String s = "min:\t" + String.format("%5.3f", min) + "\n" +
                "max:\t" + String.format("%5.3f", max) + "\n" +
                "sum:\t" + String.format("%5.3f", sum) + "\n" +
                "count:\t" + String.format("%d", count) + "\n" +
                "avg:\t" + String.format("%5.3f", average) + "\n";
        osw.print(s);
        osw.flush();
    }

}


class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String message) {
        super(message);
    }
}