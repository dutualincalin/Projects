package iofactory;

/**
 * Clasa care implementeaza Factory.
 * @author Dutu Alin Calin
 */
public final class IOFactory {
    private IOFactory() { }

    public enum IOType {
        Input, Output
    }

    /**
     * Functia creeaza o instanta in functie de tipul primit.
     * @param type Tipul de instanta;
     * @param path Calea fisierului;
     * @return Se returneaza instanta creata.
     */
    public static IO createIO(final IOType type, final String path) {
        return switch (type) {
            case Input -> new Input(path);
            case Output -> new Output(path);
        };
    }

}
