package financial;

/**
 * Clasa folosita pentru inregistrarea unui contract.
 * @author Dutu Alin Calin
 */
public final class Contract {
    private final int consumerId;
    private final int distributorId;
    private final int price;
    private int remainedContractMonths;

    public Contract(final int consumerId, final int distributorId,
                    final int price, final int remainedContractMonths) {
        this.consumerId = consumerId;
        this.distributorId = distributorId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    /**
     * Inregistreaza faptul ca a trecut o luna din perioada unui contract.
     */
    public void monthPassed() {
        remainedContractMonths--;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public int getPrice() {
        return price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public int getDistributorId() {
        return distributorId;
    }
}
