package output;

import java.util.ArrayList;

/**
 * Clasa folosita pentru scrierea output-ului folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class Producers {
    private int id;
    private int maxDistributors;
    private float priceKW;
    private String energyType;
    private int energyPerDistributor;
    private ArrayList<MonthlyStats> monthlyStats;

    public Producers(int id, int maxDistributors, float priceKW, String energyType,
                     int energyPerDistributor, ArrayList<MonthlyStats> monthlyStats) {
        this.id = id;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.energyType = energyType;
        this.monthlyStats = monthlyStats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public float getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(float priceKW) {
        this.priceKW = priceKW;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public ArrayList<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(ArrayList<MonthlyStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }
}
