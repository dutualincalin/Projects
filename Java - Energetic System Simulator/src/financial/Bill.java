package financial;

/**
 * Clasa folosita pentru crearea facturilor care vor retine platile
 * facute de clienti distribuitorilor.
 * @author Dutu Alin Calin
 */
public final class Bill {
    private final int distributorId;
    private final int sum;

    public Bill(int distributorId, int sum) {
        this.distributorId = distributorId;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public int getDistributorId() {
        return distributorId;
    }
}
