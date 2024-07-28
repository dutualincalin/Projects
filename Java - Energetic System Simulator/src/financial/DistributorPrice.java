package financial;

/**
 * Clasa folosita pentru retinerea preturilor tuturor distribuitorilor.
 * @author Dutu Alin Calin
 */
public final class DistributorPrice {
    private int id;
    private int price;

    public DistributorPrice(final int id, final int price) {
        this.id = id;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
