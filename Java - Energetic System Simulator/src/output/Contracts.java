package output;

/**
 * Clasa folosita pentru scrierea output-ului folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class Contracts {
    private int consumerId;
    private int price;
    private int remainedContractMonths;

    public Contracts(final int consumerId, final int price,
                     final int remainedContractMonths) {

        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(final int consumerId) {
        this.consumerId = consumerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void setRemainedContractMonths(final int remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }
}
