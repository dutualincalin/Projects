package actions;

import instances.Client;
import instances.Distributor;
import financial.DistributorPrice;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clasa folosita pentru retinerea operatiilor ce au in vedere informatiile din contracte.
 * @author Dutu Alin Calin
 */
public final class ContractActions {

    /**
     * Functia verifica daca vreun client e in faliment si ii inchide contractul in caz afirmativ.
     * @param distributors Lista de distribuitori;
     * @param clients Lista de clienti;
     */
    public void clientBankruptcy(final LinkedHashMap<Integer, Distributor> distributors,
                                 final LinkedHashMap<Integer, Client> clients) {
        for (Client client : clients.values()) {
            if (client.isBankrupt()) {
                if (client.getContract() != null) {
                    distributors.get(client.getContract().getDistributorId()).
                            terminateContract(client);
                    client.endContract();
                }
            }
        }
    }

    /**
     * Verifica daca un distribuitor e in faliment si ii inchide toate contractele in caz afirmativ.
     * @param distributors Lista de distribuitori;
     * @param clients Lista de clienti;
     */
    public void distributorBankruptcy(final LinkedHashMap<Integer, Distributor> distributors,
                                      final LinkedHashMap<Integer, Client> clients) {

        ArrayList<Integer> ids;

        for (Distributor distributor : distributors.values()) {
            if (distributor.isBankrupt()) {
                ids = distributor.terminateContracts();
                for (Integer id : ids) {
                    clients.get(id).endContract();
                }
            }
        }
    }

    /**
     * Din lista de clienti verifica fiecare client daca e in faliment. Daca nu este atunci
     * verifica daca are contract si in caz negativ ii se face un nou contract de la distribuitorul
     * cu cel mai mic pret. Daca are contract, dar este expirat ii se face si in acest caz un nou
     * contract dupa anularea precedentului.
     * @param distributors Lista de distribuitori;
     * @param clients Lista de clienti;
     * @param prices Lista de preturi;
     */
    public void clientContractsUpdate(final LinkedHashMap<Integer, Distributor> distributors,
                                      final LinkedHashMap<Integer, Client> clients,
                                      final ArrayList<DistributorPrice> prices) {

        for (Client client : clients.values()) {
            if (!client.isBankrupt()) {
                if (client.getContract() == null) {
                    distributors.get(prices.get(0).getId()).
                            addContract(client, prices.get(0).getPrice());

                    client.setContract(prices.get(0).getId(), prices.get(0).getPrice(),
                            distributors.get(prices.get(0).getId()).getContractLength());
                } else if (client.getContract().getRemainedContractMonths() == 0) {
                    distributors.get(client.getContract().getDistributorId()).
                            terminateContract(client);

                    client.endContract();

                    client.setContract(prices.get(0).getId(), prices.get(0).getPrice(),
                            distributors.get(prices.get(0).getId()).getContractLength());

                    distributors.get(prices.get(0).getId()).addContract(client,
                            prices.get(0).getPrice());
                }
            }
        }
    }
}

