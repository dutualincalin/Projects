package strategies;

import instances.Distributor;

/**
 * Clasa folosita de Strategy Pattern.
 * @author Dutu Alin Calin
 */
public interface ChoosingStrategy {
    /**
     * Functia sorteaza producatorii in functie de strategie
     * si ii adauga distribuitorului cati producatori are nevoie.
     * @param distributor Distribuitorul care are nevoie de noi producatori;
     */
    void choose(Distributor distributor);
}
