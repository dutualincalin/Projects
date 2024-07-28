package input;

/**
 * Clasa folosita pentru parsarea datelor folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class DistributorChanges {
    private int id;
    private int infrastructureCost;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }
}
