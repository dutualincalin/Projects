package actions;

import common.Constants;
import instances.Distributor;
import instances.Producer;
import strategies.ChoosingStrategy;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clasa folosita pentru executarea procedurilor pentru instante.
 * @author Dutu Alin Calin
 */
public class InstanceProcedures {
    /**
     * Functia actualizeaza lista de producatori a distribuitorilor care trebuie sa caute noi
     * producatori. Se actualizeaza distribuitorii si producatorii si se apeleaza Design
     * Pattern-ul Strategy pentru ca un distribuitor sa isi aleaga noi producatori.
     * @param distributors Lista de distribuitori;
     * @param producers Lista de producatori;
     */
    public void newDistributorProducers(final LinkedHashMap<Integer, Distributor> distributors,
                                        ArrayList<Producer> producers) {
        for (Distributor distributor : distributors.values()) {
            if (distributor.getChangeProducers() == 1) {
                distributor.resetProducers();
                chooseProducers(distributor, producers);
            }
        }
    }

    /**
     * Aceasta functie face parte din Design Pattern-ul Strategy si se apeleaza prin intermediul ei
     * strategia de alegere a producatorilor ceruta de distribuitor. In final se actualizeaza si
     * costul de productie al distribuitorului.
     * @param distributor Distribuitorul care cauta noi producatori;
     * @param producers Lista de producatori;
     */
    public void chooseProducers(Distributor distributor, ArrayList<Producer> producers) {
        ChoosingStrategy payment;

        if (distributor.getProducerStrategy().equalsIgnoreCase(Constants.GREEN)) {
            payment = new GreenStrategy(producers);
        } else if (distributor.getProducerStrategy().equalsIgnoreCase(Constants.PRICE)) {
            payment = new PriceStrategy(producers);
        } else {
            payment = new QuantityStrategy(producers);
        }

        payment.choose(distributor);
        distributor.setProductionCost();
    }

    /**
     * Functia face statistica cu id-urile distribuitorilor care au contract in fiecare luna (runda)
     * pentru fiecare distribuitor.
     * @param producers Lista de producatori;
     * @param month Numarul lunii (rundei);
     */
    public void addMonthlyStats(LinkedHashMap<Integer, Producer> producers, int month) {
        for (Producer producer : producers.values()) {
            producer.addMonthlyStats(month);
        }
    }
}
