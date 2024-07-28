package input;

import instances.Client;
import instances.Distributor;
import instances.MonthUpdate;
import instances.Producer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clasa folosita pentru parsarea datelor de input si adaugarea acestora in liste.
 * @author Dutu Alin Calin
 */
public final class JacksonToDataConverter {
    private int months;
    private final LoaderIn loader;
    private final LinkedHashMap<Integer, Client> clients;
    private final LinkedHashMap<Integer, Distributor> distributors;
    private final LinkedHashMap<Integer, Producer> producers;
    private final ArrayList<MonthUpdate> updates;


    public JacksonToDataConverter(final LoaderIn loader) {
        this.loader = loader;
        clients = new LinkedHashMap<>();
        distributors = new LinkedHashMap<>();
        producers = new LinkedHashMap<>();
        updates = new ArrayList<>();
    }

    /**
     * Preia datele din instanta de parsare, loader, adaugand fiecare data in lista corespunzatoare.
     */
    public void populate() {
        months = loader.getNumberOfTurns();

        for (Consumers consumer : loader.getInitialData().getConsumers()) {
            clients.put(consumer.getId(), new Client(consumer.getId(),
                    consumer.getMonthlyIncome(), consumer.getInitialBudget()));
        }

        for (Distributors distributor : loader.getInitialData().getDistributors()) {
            distributors.put(distributor.getId(),
                    new Distributor(distributor.getId(), distributor.getContractLength(),
                            distributor.getInitialBudget(),
                            distributor.getInitialInfrastructureCost(),
                            distributor.getEnergyNeededKW(),
                            distributor.getProducerStrategy()));
        }

        for (Producers producer : loader.getInitialData().getProducers()) {
            producers.put(producer.getId(), new Producer(producer.getId(), producer.getEnergyType(),
                    producer.getMaxDistributors(), producer.getPriceKW(),
                    producer.getEnergyPerDistributor()));
        }


        for (MonthlyUpdates update : loader.getMonthlyUpdates()) {
            ArrayList<Client> newClients = new ArrayList<>();
            ArrayList<Distributor> updateDistributors = new ArrayList<>();
            ArrayList<Producer> updateProducers = new ArrayList<>();

            for (NewConsumers newClient : update.getNewConsumers()) {
                newClients.add(new Client(newClient.getId(), newClient.getMonthlyIncome(),
                        newClient.getInitialBudget()));
            }

            for (DistributorChanges changes : update.getDistributorChanges()) {
                updateDistributors.add(new Distributor(changes.getId(),
                        changes.getInfrastructureCost()));
            }

            for (ProducerChanges changes : update.getProducerChanges()) {
                updateProducers.add(new Producer(changes.getId(),
                        changes.getEnergyPerDistributor()));
            }

            updates.add(new MonthUpdate(newClients, updateDistributors, updateProducers));
        }
    }

    public int getMonths() {
        return months;
    }

    public LinkedHashMap<Integer, Client> getClients() {
        return clients;
    }

    public LinkedHashMap<Integer, Distributor> getDistributors() {
        return distributors;
    }

    public LinkedHashMap<Integer, Producer> getProducers() {
        return producers;
    }

    public ArrayList<MonthUpdate> getUpdates() {
        return updates;
    }
}
