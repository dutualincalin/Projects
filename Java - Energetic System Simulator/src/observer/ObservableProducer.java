package observer;

import instances.Distributor;

import java.util.LinkedHashMap;

/**
 * Clasa abstracta folosita in Design Pattern-ul Observer pentru
 * instanta de tip Observable (pentru producatori in acest caz).
 * @author Dutu Alin Calin
 */
public abstract class ObservableProducer {
    private final LinkedHashMap<Integer, Distributor> distributors = new LinkedHashMap<>();

    /**
     * Functia adauga noi observatori (Distribuitori in acest caz)
     * in mapa de observatori.
     * @param observer Noul observator (Distribuitor);
     */
    public final void addDistributor(Distributor observer) {
        distributors.put(observer.getId(), observer);
    }

    public final LinkedHashMap<Integer, Distributor> getDistributors() {
        return distributors;
    }
}
