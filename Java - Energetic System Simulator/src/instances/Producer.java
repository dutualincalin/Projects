package instances;

import common.Comparators;
import observer.ObservableProducer;
import statistics.MonthlyStats;

import java.util.ArrayList;

/**
 * Clasa folosita pentru prelucrarea datelor producatorilor de energie.
 * @author Dutu Alin Calin
 */
public final class Producer extends ObservableProducer {
    private final int id;
    private String energyType;
    private int maxDistributors;
    private float priceKW;
    private int energyPerDistributor;
    private ArrayList<MonthlyStats> monthlyStats;

    public Producer(final int id, final String energyType,
                    final int maxDistributors, final float priceKW,
                    final int energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        monthlyStats = new ArrayList<>();
    }

    public Producer(int id, int energyPerDistributor) {
        this.id = id;
        this.energyPerDistributor = energyPerDistributor;
    }

    /**
     * Functia verifica daca energia produsa de producator este regenerabila
     * @return Returneaza 1 daca energia produsa este regenerabila si 0 in
     * caz contrar.
     */
    public int renewableEnergy() {
        return switch ((energyType.toUpperCase())) {
            case "WIND", "SOLAR", "HYDRO" -> 1;
            default -> 0;
        };
    }

    /**
     * Aceasta functie se incadreaza in schema Design Pattern-ului Observer.
     * Functia schimba valoarea debitului pentru un distribuitor si notifica
     * toti observatorii (distribuitorii de acest lucru).
     * @param value Valoarea debitului de energie pentru un distribuitor;
     */
    public void updateEnergyDebit(int value) {
        energyPerDistributor = value;
        for (Distributor observers : getDistributors().values()) {
            observers.update();
        }
    }

    /**
     * Functia adauga in baza de date a producatorilor o statistica lunara
     * privind o lista cu id-urile distribuitorilor care cumpara de la
     * producatorul curent.
     * @param month Numarul lunii;
     */
    public void addMonthlyStats(int month) {
        ArrayList<Integer> distributorIds = new ArrayList<>();
        for (Distributor distributors : getDistributors().values()) {
            distributorIds.add(distributors.getId());
        }

        Comparators comparator = new Comparators();
        distributorIds.sort(comparator :: sortDistributorIdsAsc);
        monthlyStats.add(new MonthlyStats(month, distributorIds));
    }

    public int getId() {
        return id;
    }

    public String getEnergyType() {
        return energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public float getPriceKW() {
        return priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public ArrayList<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }
}
