package iofactory;

/**
 * Interfata pentru instantele care preiau datele de intrare din input
 * si care adauga in output datele de iesire.
 * @author Dutu Alin Calin
 */
public abstract class IO {

    /**
     * Functie abstracta folosita pentru a parsa datele intr-o instanta
     * sau sa adauge rezultatul final intr-un fisier folosind o instanta.
     * Se foloseste metoda Jackson pentru ambele tipuri de executie.
     * @throws Exception Spune compilatorului ca programul poate avea
     * exceptii.
     */
    public abstract void execute() throws Exception;
}
