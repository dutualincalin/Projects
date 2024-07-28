package input;

/**
 * Clasa folosita pentru parsarea datelor folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class ProducerChanges {
    private int id;
    private int energyPerDistributor;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
