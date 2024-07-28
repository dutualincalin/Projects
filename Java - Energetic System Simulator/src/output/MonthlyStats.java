package output;

import java.util.ArrayList;

/**
 * Clasa folosita pentru scrierea output-ului folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class MonthlyStats {
    private int month;
    private ArrayList<Integer> distributorsIds;

    public MonthlyStats(int month, ArrayList<Integer> distributorsIds) {
        this.month = month;
        this.distributorsIds = distributorsIds;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<Integer> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(ArrayList<Integer> distributorsIds) {
        this.distributorsIds = distributorsIds;
    }
}
