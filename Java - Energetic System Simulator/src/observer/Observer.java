package observer;

/**
 * Clasa abstracta folosita in Design Pattern-ul Observer pentru
 * instanta de tip Observer (pentru distribuitori in acest caz).
 * @author Dutu Alin Calin
 */
public interface Observer {

    /**
     * Metoda de notificare a observatorilor.
     */
    void update();
}
