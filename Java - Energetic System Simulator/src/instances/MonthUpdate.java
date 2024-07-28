package instances;

import java.util.ArrayList;

/**
 * Clasa folosita pentru retinerea actualizarilor lunare ale sistemului.
 * @author Dutu Alin Calin
 */
public final class MonthUpdate {
    private final ArrayList<Client> newConsumers;
    private final ArrayList<Distributor> distributorChanges;
    private final ArrayList<Producer> producerChanges;

    public MonthUpdate(final ArrayList<Client> newConsumers,
                       final ArrayList<Distributor> distributorChanges,
                       final ArrayList<Producer> producerChanges) {
        this.newConsumers = newConsumers;
        this.distributorChanges = distributorChanges;
        this.producerChanges = producerChanges;
    }

    public ArrayList<Client> getNewConsumers() {
        return newConsumers;
    }

    public ArrayList<Distributor> getDistributorChanges() {
        return distributorChanges;
    }

    public ArrayList<Producer> getProducerChanges() {
        return producerChanges;
    }
}
