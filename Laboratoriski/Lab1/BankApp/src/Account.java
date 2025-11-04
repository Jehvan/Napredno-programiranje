import java.util.Objects;
import java.util.Random;

public class Account {
    public String username;
    public long ID;
    public Double balance;

    Account(String username, Double balance) {
        this.username = username;
        this.balance = balance;
        this.ID = new Random().nextInt();
    }

    public double getBalance() {
        return balance;
    }
    public String getName() {
        return username;
    }
    public long getId() {
        return ID;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    @Override
    public String toString() {
        return "Name:" + this.username + "\nBalance:" + this.balance + "\n";
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username) && Objects.equals(ID, account.ID) && Objects.equals(balance, account.balance);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, ID, balance);
    }
}
