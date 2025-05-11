package payment;

public class Debt {
    public String source;
    public double amount;

    public Debt(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }
}
