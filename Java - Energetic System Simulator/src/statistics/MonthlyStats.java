package statistics;

import java.util.ArrayList;

/**
 * Clasa folosita pentru retinerea statisticilor lunare
 * privind id-urile distribuitorilor abonati la producatori.
 * @author Dutu Alin Calin
 */
public final class MonthlyStats {
    private final int month;
    private final ArrayList<Integer> distributorIds;

    public MonthlyStats(int month, ArrayList<Integer> distributorIds) {
        this.month = month;
        this.distributorIds = distributorIds;
    }

    public int getMonth() {
        return month;
    }

    public ArrayList<Integer> getDistributorIds() {
        return distributorIds;
    }
}
