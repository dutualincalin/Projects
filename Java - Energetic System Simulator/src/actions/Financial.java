package actions;

import common.Comparators;
import financial.Bill;
import financial.DistributorPrice;
import instances.Client;
import instances.Distributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Clasa care se ocupa cu operatiile pe partea de finante.
 * @author Dutu Alin Calin
 */
public final class Financial {
    private Comparators comparator = new Comparators();
    /**
     * In aceasta functie fiecare client isi primeste salariul.
     * @param clients Lista de clienti;
     */
    public void clientsGetSalaries(final LinkedHashMap<Integer, Client> clients) {
        for (Client client : clients.values()) {
            if (!client.isBankrupt()) {
                client.clientGetsSalary();
            }
        }
    }

    /**
     * In aceasta functie fiecare client isi va plati factura si i se va scadea o luna din
     * durata contractului sau.
     * @param clients Lista de clienti;
     * @param distributors Lista de distribuitori;
     */
    public void clientPaysBills(final LinkedHashMap<Integer, Client> clients,
                                final LinkedHashMap<Integer, Distributor> distributors) {

        Bill bill;
        for (Client client : clients.values()) {
            if (!client.isBankrupt()) {
                bill = client.pays();

                if (bill != null) {
                    distributors.get(bill.getDistributorId()).payedBill(bill.getSum());
                }

                client.getContract().monthPassed();
                Objects.requireNonNull(distributors.get(client.getContract().getDistributorId()).
                        getContract(client.getId())).monthPassed();
            }
        }
    }

    /**
     * Se actualizeaza bugetele fiecarui distribuitor.
     * @param distributors Lista de distribuitori;
     */
    public void distributorsBudgetUpdates(final LinkedHashMap<Integer, Distributor> distributors) {
        for (Distributor distributor : distributors.values()) {
            if (!distributor.isBankrupt()) {
                distributor.pays();
            }
        }
    }

    /**
     * In cazul in care programul este in runda initiala se adauga preturile distribuitorilor intr-o
     * lista de preturi. In caz contrar se vor actualiza preturile si se vor elimina preturile
     * distribuitorilor au ajuns la faliment.
     * @param distributors Lista de distribuitori;
     * @param prices Lista de preturi;
     * @param round Numarul rundei;
     */
    public void updatePrices(final LinkedHashMap<Integer, Distributor> distributors,
                             final ArrayList<DistributorPrice> prices, final int round) {
        if (round != 0) {
            Iterator<DistributorPrice> iter = prices.iterator();

            while (iter.hasNext()) {
                DistributorPrice price = iter.next();

                if (!distributors.get(price.getId()).isBankrupt()) {
                    price.setPrice(distributors.get(price.getId()).getPrice());
                } else {
                    iter.remove();
                }
            }
        } else {
            for (Distributor distributorPrice : distributors.values()) {
                prices.add(new DistributorPrice(distributorPrice.getId(),
                        distributorPrice.getPrice()));
            }
        }

        prices.sort(comparator::sortPrice);
    }
}
