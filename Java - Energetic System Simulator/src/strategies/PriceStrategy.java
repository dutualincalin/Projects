package strategies;

import common.Comparators;
import instances.Distributor;
import instances.Producer;

import java.util.ArrayList;

/**
 * Clasa care implementeaza PRICE strategy pentru distribuitori.
 * @author Dutu Alin Calin
 */
public final class PriceStrategy implements ChoosingStrategy {
    private final ArrayList<Producer> producers;

    public PriceStrategy(ArrayList<Producer> producers) {
        this.producers = producers;
    }

    /**
     * Metoda sorteaza lista de producatori dupa urmatoarele criterii:
     * pret per KW, cantitate, id.
     * @param distributor Distribuitorul care are nevoie de noi producatori;
     */
    @Override
    public void choose(final Distributor distributor) {
        int sum = 0;
        Comparators comparators = new Comparators();

        producers.sort(comparators::sortProducerPrice);

        for (Producer producer: producers) {
            if (producer.getMaxDistributors() != producer.getDistributors().size()) {
                producer.addDistributor(distributor);
                distributor.getProducers().put(producer.getId(), producer);
                sum += producer.getEnergyPerDistributor();

                if (sum >= distributor.getEnergyNeededKW()) {
                    break;
                }
            }
        }
    }
}
