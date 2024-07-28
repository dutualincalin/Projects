package output;

import java.util.ArrayList;

/**
 * Clasa folosita pentru scrierea output-ului folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class LoaderOut {
    private ArrayList<Consumers> consumers;
    private ArrayList<Distributors> distributors;
    private ArrayList<Producers> energyProducers;
    private static LoaderOut loader = null;

    private LoaderOut() { }

    /**
     * Aceasta functie se asigura ca de fiecare data cand este apelata returneaza una si aceeasi
     * instanta.(Singleton Pattern)
     * @return Se returneaza o instanta de tip LoaderOut.
     */
    public static LoaderOut getLoader() {
        if (loader == null) {
            loader = new LoaderOut();
        }

        return loader;
    }

    public static void setLoader(final LoaderOut loader) {
        LoaderOut.loader = loader;
    }

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

    public ArrayList<Producers> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(final ArrayList<Producers> energyProducers) {
        this.energyProducers = energyProducers;
    }
}
