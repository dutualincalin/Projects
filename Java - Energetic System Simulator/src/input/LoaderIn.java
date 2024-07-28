package input;

import java.util.ArrayList;

/**
 * Clasa folosita pentru parsarea datelor folosind metoda Jackson
 * @author Dutu Alin Calin
 */
public final class LoaderIn {
    private int numberOfTurns;
    private input.InitialData initialData;
    private ArrayList<MonthlyUpdates> monthlyUpdates;
    private static LoaderIn loader = null;

    private LoaderIn() { }

    /**
     * Aceasta functie se asigura ca de fiecare data cand este apelata returneaza una si aceeasi
     * instanta.(Singleton Pattern)
     * @return Se returneaza o instanta de tip LoaderIn.
     */
    public static LoaderIn getLoader() {
        if (loader == null) {
            loader = new LoaderIn();
        }

        return loader;
    }

    public static void setLoader(final LoaderIn loader) {
        LoaderIn.loader = loader;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public InitialData getInitialData() {
        return initialData;
    }

    public void setInitialData(final InitialData initialData) {
        this.initialData = initialData;
    }

    public ArrayList<MonthlyUpdates> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final ArrayList<MonthlyUpdates> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }
}
