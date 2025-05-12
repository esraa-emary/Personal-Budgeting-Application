package payment;

public class Donate {
    public String source;
    public double amount;

    public Donate(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }
}
