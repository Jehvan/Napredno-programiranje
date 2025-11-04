import java.util.Objects;

abstract class Transaction {
    protected long fromId;
    protected long toId;
    protected String description;
    protected Double amount;

    Transaction(long fromId, long toId, String description, Double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }
    public abstract Double provision();
}

class FlatAmountProvisionTransaction extends Transaction {
    public Double flatProvision;


    public FlatAmountProvisionTransaction(long fromId, long toId, Double amount,Double flatProvision) {
        super(fromId, toId, "FlatAmount",amount);
        this.flatProvision = flatProvision;
    }

    public Double getflatProvision() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Double.compare(flatProvision,that.flatProvision) == 0;
    }

    @Override
    public Double provision() {
        return flatProvision;
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    public int flatPercent;

    public FlatPercentProvisionTransaction(long fromId, long toId, Double amount, int centsPerDolar) {
        super(fromId, toId, "FlatPercent", amount);
        this.flatPercent = centsPerDolar;
    }

    public int getPercent() {
        return flatPercent;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return Objects.equals(flatPercent, that.flatPercent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flatPercent);
    }
    @Override
    public Double provision() {
        return flatPercent / 100.0 * amount;
    }
}