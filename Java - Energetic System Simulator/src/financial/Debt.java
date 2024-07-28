package financial;

/**
 * Clasa folosita pentru inregistrarea datoriilor clientilor.
 * @author Dutu Alin Calin
 */
public final class Debt {
    private final int distributorId;
    private final int sum;

    public Debt(final int distributorId, final int sum) {
        this.distributorId = distributorId;
        this.sum = sum;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public int getSum() {
        return sum;
    }


}
