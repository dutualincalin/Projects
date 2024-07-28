package output;

/**
 * Clasa folosita pentru scrierea output-ului folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class Consumers {
    private int id;
    private boolean isBankrupt;
    private int budget;

    public Consumers(final int id, final boolean isBankrupt, final int budget) {
        this.id = id;
        this.isBankrupt = isBankrupt;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }
}
