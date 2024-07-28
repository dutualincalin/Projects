package actions;

import instances.Client;
import instances.Distributor;
import instances.MonthUpdate;
import instances.Producer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clasa care contine toate operatiile de actualizare a datelor.
 * @author Dutu Alin Calin
 */
public final class Update {
    /**
     * Adauga noi clienti in joc si actualizeaza costurile si preturile distribuitorilor.
     * @param clients Lista clientilor;
     * @param distributors Lista distribuitorilor;
     * @param updates Lista actualizarilor;
     * @param round Numarul rundei;
     */
    public void updateData(final LinkedHashMap<Integer, Client> clients,
                           final LinkedHashMap<Integer, Distributor> distributors,
                           final ArrayList<MonthUpdate> updates, final int round) {

        for (Client clientUpdate : updates.get(round - 1).getNewConsumers()) {
            clients.put(clientUpdate.getId(), clientUpdate);
        }

        for (Distributor distributorUpdate : updates.get(round - 1).getDistributorChanges()) {
            distributors.get(distributorUpdate.getId())
                    .setInfrastructureCost(distributorUpdate.getInfrastructureCost());
        }
    }

    /**
     * Aceasta functie actualizeaza debitul per distribuitor al producatorilor primit din
     * update-uri. De asemenea, prin intermediul design-ului Observer se notifica toti
     * distribuitorii producatorului caruia i se actualizeaza debitul de energie.
     * @param producers Lista de producatori;
     * @param updates Lista cu actualizarile lunare;
     * @param round Numarul rundei;
     */
    public void updateProducers(final LinkedHashMap<Integer, Producer> producers,
                                final ArrayList<MonthUpdate> updates, final int round) {

        for (Producer producerUpdate : updates.get(round - 1).getProducerChanges()) {
            producers.get(producerUpdate.getId())
                    .updateEnergyDebit(producerUpdate.getEnergyPerDistributor());
        }
    }
}
