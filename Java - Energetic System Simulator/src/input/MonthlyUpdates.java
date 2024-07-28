package input;

import java.util.ArrayList;

/**
 * Clasa folosita pentru parsarea datelor folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class MonthlyUpdates {
    private ArrayList<NewConsumers> newConsumers;
    private ArrayList<DistributorChanges> distributorChanges;
    private ArrayList<ProducerChanges> producerChanges;

    public ArrayList<NewConsumers> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final ArrayList<NewConsumers> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public ArrayList<DistributorChanges> getDistributorChanges() {
        return distributorChanges;
    }

    public void setDistributorChanges(final ArrayList<DistributorChanges> distributorChanges) {
        this.distributorChanges = distributorChanges;
    }

    public ArrayList<ProducerChanges> getProducerChanges() {
        return producerChanges;
    }

    public void setProducerChanges(final ArrayList<ProducerChanges> producerChanges) {
        this.producerChanges = producerChanges;
    }
}
