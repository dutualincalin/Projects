package instances;

import financial.Contract;
import observer.Observer;
import common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Clasa folosita pentru retinerea informatiilor pentru distribuitori.
 * @author Dutu Alin Calin
 */
public final class Distributor implements Observer {
    private final int id;
    private int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int energyNeededKW;
    private String producerStrategy;
    private int changeProducers;
    private boolean isBankrupt;
    private ArrayList<Contract> contracts;
    private HashMap<Integer, Producer> producers;

    public Distributor(final int id, final int contractLength, final int budget,
                       final int infrastructureCost, final int energyNeededKW,
                       final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.contracts = new ArrayList<>();
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = false;
        producers = new HashMap<>();
        changeProducers = 0;
    }

    public Distributor(final int id, final int infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    /**
     * Se calculeaza noul pret pentru un contract. Se folosesc formulele din cerinta.
     * @return Se retuneaza noul pret pentru un contract.
     */
    public int getPrice() {
        if (contracts.size() == 0) {
            return (infrastructureCost + productionCost
                    + (int) Math.round(Math.floor(Constants.PROFIT_NUMBER * productionCost)));
        }

        return (int) Math.round(Math.floor((float) (infrastructureCost / contracts.size()))
                + productionCost
                + (int) Math.round(Math.floor(Constants.PROFIT_NUMBER * productionCost)));
    }

    /**
     * Furnizorul primeste rata platita de un consumator.
     * @param value Rata unui client.
     */
    public void payedBill(final int value) {
        budget += value;
    }

    /**
     * Functia actualizeaza bugetul distribuitorului si verifica daca acesta a dat faliment.
     */
    public void pays() {
        budget -= (infrastructureCost + productionCost * contracts.size());

        if (budget < 0) {
            isBankrupt = true;
        }
    }

    /**
     * Functia seteaza noul cost de productie.
     */
    public void setProductionCost() {
        int cost = 0;

        for (Producer producer : this.producers.values()) {
            cost += producer.getPriceKW() * producer.getEnergyPerDistributor();
        }

        productionCost = (int) Math.round(Math.floor((float) cost / Constants.DIVIDING_NUMBER));
    }

    /**
     * Functia stabileste daca furnizorul de energie a dat faliment si returneaza rezultatul.
     * @return Se returneaza true daca furnizorul a dat faliment si false in caz contrar.
     */
    public boolean isBankrupt() {
        if (budget < 0) {
            isBankrupt = true;
        }

        return isBankrupt;
    }

    /**
     * Aceasta functie inregistreaza un nou contract pentru un consumator. Daca un consumator a avut
     * datorii la distribuitor, ele vor fi adaugate ca penalizari in informatiile sale.
     * @param client Informatiile clientului;
     * @param price Pretul contractului;
     */
    public void addContract(final Client client, final int price) {
        contracts.add(new Contract(client.getId(), this.id, price, contractLength));
    }

    /**
     * Se returneaza contractul unui client al carui id este primit ca parametru.
     * @param idClient Id-ul unui client;
     * @return Se returneaza contractul clientului cautat.
     */
    public Contract getContract(final int idClient) {
        for (Contract contract : contracts) {
            if (contract.getConsumerId() == idClient) {
                return contract;
            }
        }

        return null;
    }

    /**
     * Functia elimina un contract. In cazul in care consumatorul in cauza are datorii, acestea vor
     * fi retinute intr-un vector, impreuna cu id-ul acestuia.
     * @param client Informatiile clientului;
     */
    public void terminateContract(final Client client) {
        contracts.removeIf(contract -> contract.getConsumerId() == client.getId());
    }

    /**
     * Aceasta functie elimina toate contractele distribuitorului si face o lista cu toate id-urile
     * consumatorilor afectati pentru a le fi scoase contractele din baza lor de date. Functia se
     * foloseste NUMAI in cazul in care distribuitorul da faliment.
     * @return Se returneaza un vector cu id-urile consumatorilor ale caror contracte au fost
     * sterse.
     */
    public ArrayList<Integer> terminateContracts() {
        ArrayList<Integer> ids = new ArrayList<>();
        Iterator<Contract> iter = contracts.iterator();

        while (iter.hasNext()) {
            Contract contract = iter.next();
            ids.add(contract.getConsumerId());

            iter.remove();
        }

        return ids;
    }

    /**
     * Functia seteaza flag-ul care notifica sistemul ca acest distributor.
     * este in cautare de producatori.
     */
    public void update() {
        changeProducers = 1;
    }

    /**
     * Functia seteaza flag-ul pentru cautarea producatorilor la 0 si curata lista de
     * producatori a distribuitorului. In plus, se elimina si distribuitorul in cauza
     * din listele vechilor producatori.
     */
    public void resetProducers() {
        changeProducers = 0;
        for (Producer producer : producers.values()) {
            producer.getDistributors().remove(id);
        }
        producers.clear();
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public void setInfrastructureCost(final int infrastructureCosts) {
        infrastructureCost = infrastructureCosts;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public int getChangeProducers() {
        return changeProducers;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public HashMap<Integer, Producer> getProducers() {
        return producers;
    }
}
