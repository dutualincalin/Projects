package output;

import instances.Client;
import financial.Contract;
import instances.Distributor;
import financial.DistributorPrice;
import instances.Producer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clasa folosita pentru crearea unei instante din toate listele folosite in program care va crea
 * fisierul de iesire folosind metoda Jackson.
 * @author Dutu Alin Calin
 */
public final class DataToJacksonConverter {

    /**
     * Functia creeaza instanta de output si incarca toate listele de entitati folosite in output.
     * @param clients Lista de clienti;
     * @param distributors Lista de distribuitori;
     * @return Se returneaza instanta de output cu liste de entitati incarcate.
     */
    public LoaderOut loader(final LinkedHashMap<Integer, Client> clients,
                            final LinkedHashMap<Integer, Distributor> distributors,
                            final LinkedHashMap<Integer, Producer> producers,
                            final ArrayList<DistributorPrice> prices) {
        LoaderOut loader = LoaderOut.getLoader();
        loader.setConsumers(consumers(clients));
        loader.setDistributors(distributors(distributors, prices));
        loader.setEnergyProducers(producers(producers));
        return loader;
    }

    /**
     * Incarca lista de consumatori sub forma unei liste care va fi adaugata in instanta
     * de output.
     * @param clients Lista de clienti;
     * @return Lista de clienti pentru output.
     */
    public ArrayList<Consumers> consumers(
            final LinkedHashMap<Integer, Client> clients) {
        ArrayList<Consumers> consumers = new ArrayList<>();
        for (Client client : clients.values()) {
            consumers.add(new Consumers(client.getId(),
                    client.isBankrupt(), client.getBudget()));
        }

        return consumers;
    }

    /**
     * Incarca lista de distribuitori sub forma unei liste care va fi adaugata in instanta
     * de output.
     * @param distributors Lista de distribuitori;
     * @return Lista de distribuitori pentru output.
     */
    public ArrayList<Distributors> distributors(
            final LinkedHashMap<Integer, Distributor> distributors,
            final ArrayList<DistributorPrice> prices) {

        ArrayList<Distributors> distribution = new ArrayList<>();
        for (Distributor distributor : distributors.values()) {
            distribution.add(new Distributors(distributor.getId(), distributor.getEnergyNeededKW(),
                    prices.get(distributor.getId()).getPrice(), distributor.getBudget(),
                    distributor.getProducerStrategy(), distributor.isBankrupt(),
                    getContracts(distributor.getContracts())));
        }

        return distribution;
    }

    /**
     * Incarca lista de contracte sub forma unei liste care va fi adaugata in instanta
     * de output.
     * @param contracts Lista de contracte;
     * @return Lista de contracte pentru output.
     */
    public ArrayList<Contracts> getContracts(final ArrayList<Contract> contracts) {
        ArrayList<Contracts> contractList = new ArrayList<>();
        for (Contract contract : contracts) {
            contractList.add(new Contracts(contract.getConsumerId(), contract.getPrice(),
                    contract.getRemainedContractMonths()));
        }

        return contractList;
    }

    /**
     * Functia preia producatorii si ii pune intr-o noua lista care va fi inserata
     * in instanta de output folosita de metoda Jackson.
     * @param producers Lista de producatori;
     * @return Returneaza o lista de producatori care va fi folosita de Jackson.
     */
    public ArrayList<Producers> producers(final LinkedHashMap<Integer, Producer> producers) {
        ArrayList<Producers> production = new ArrayList<>();
        for (Producer producer : producers.values()) {
            production.add(new Producers(producer.getId(), producer.getMaxDistributors(),
                    producer.getPriceKW(), producer.getEnergyType(),
                    producer.getEnergyPerDistributor(), getStats(producer.getMonthlyStats())));
        }

        return production;
    }

    /**
     * Functia preia statisticile lunare si le pune intr-o noua lista care va fi inserata
     * in instanta de output folosita de metoda Jackson.
     * @param stats Lista cu statistici;
     * @return Returneaza lista cu statistici folosita de Jackson.
     */
    public ArrayList<MonthlyStats> getStats(final ArrayList<statistics.MonthlyStats> stats) {
        ArrayList<MonthlyStats> statistics = new ArrayList<>();
        for (statistics.MonthlyStats statistic : stats) {
            statistics.add(new MonthlyStats(statistic.getMonth(), statistic.getDistributorIds()));
        }

        return statistics;
    }
}
