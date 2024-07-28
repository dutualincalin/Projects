package input;

import java.util.ArrayList;

/**
 * Clasa folosita pentru parsarea datelor folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class InitialData {
    private ArrayList<Consumers> consumers;
    private ArrayList<Distributors> distributors;
    private ArrayList<Producers> producers;

    public ArrayList<Consumers> getConsumers() {
        return consumers;
    }

    public void setConsumers(final ArrayList<Consumers> consumers) {
        this.consumers = consumers;
    }

    public ArrayList<Distributors> getDistributors() {
        return distributors;
    }

    public void setDistributors(final ArrayList<Distributors> distributors) {
        this.distributors = distributors;
    }

    public ArrayList<Producers> getProducers() {
        return producers;
    }

    public void setProducers(final ArrayList<Producers> producers) {
        this.producers = producers;
    }
}
