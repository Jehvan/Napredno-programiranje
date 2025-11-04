public class Bank {
    String name;
    Account[] accounts;
    public Double totalTransfers = 0.0;
    public Double totalProvision = 0.0;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = accounts;
    }
    public boolean makeTransaction(Transaction t){
        Long userFromId = t.getFromId();
        Long userToId = t.getToId();
        Account userFrom = null;
        Account userTo = null;
        for (Account account : accounts) {
            if (account.getId() == userFromId) {
                userFrom = account;
            }
            if (account.getId() == userToId) {
                userTo = account;
            }
        }
        if (userFrom != null && userTo != null && (userFrom.balance >= t.amount)) {
            userTo.balance += t.amount;
            userFrom.balance -= t.amount;
            this.totalTransfers += t.amount;
            String transaction_type = t.getDescription();
            if (transaction_type.equals("FlatPercent")){
                FlatPercentProvisionTransaction account = (FlatPercentProvisionTransaction) t;
                this.totalProvision += t.amount * (account.getPercent() / 100.0);
                userFrom.balance -= t.amount * (account.getPercent() / 100.0);
            } else if (transaction_type.equals("FlatAmount")){
                FlatAmountProvisionTransaction account = (FlatAmountProvisionTransaction) t;
                this.totalProvision += t.amount + account.getflatProvision();
                userFrom.balance -= t.amount * account.getflatProvision();
            }
            return true;
        }
        return false;
    }

    public Double totalTransfers() {
        return totalTransfers;
    }

    public Double totalProvision() {
        return totalProvision;
    }

    public String getName() {
        return name;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name:").append(name).append("\n\n");
        for (Account a : accounts) {
            s.append(a);
        }
        return s.toString();
    }
}
