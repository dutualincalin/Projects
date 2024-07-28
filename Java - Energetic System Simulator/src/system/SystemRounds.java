package system;

import actions.ContractActions;
import actions.Financial;
import actions.InstanceProcedures;
import actions.Update;
import financial.DistributorPrice;
import instances.Client;
import instances.Distributor;
import instances.MonthUpdate;
import instances.Producer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Aceasta clasa este centrul de prelucrare a datelor pentru program
 * @author Dutu Alin Calin
 */
public final class SystemRounds {
    /**
     * Aceasta functie reprezinta centrul de prelucrare a datelor programului. Aici
     * se prelucreaza toata informatia programului. Functia executa toate actiunile
     * necesare pentru fiecare runda a jocului
     * @param months Numarul de luni;
     * @param clients Lista clientilor;
     * @param distributors Lista distribuitorilor;
     * @param producers Lista producatorilor;
     * @param producerList Lista clientilor folosita pentru cazul in care un
     *                     distribuitor isi cauta producatori;
     * @param updates Lista cu informatiile de actualizat la fiecare runda;
     * @param prices Lista de preturi a fiecarui distribuitor;
     */
    public void systemExecute(int months, LinkedHashMap<Integer, Client> clients,
                              LinkedHashMap<Integer, Distributor> distributors,
                              LinkedHashMap<Integer, Producer> producers,
                              ArrayList<Producer> producerList,
                              ArrayList<MonthUpdate> updates, ArrayList<DistributorPrice> prices) {

        int i;
        int check;
        Update updater = new Update();
        Financial finance = new Financial();
        ContractActions contractActions = new ContractActions();
        InstanceProcedures instanceProcedures = new InstanceProcedures();

        for (i = 0; i <= months; i++) {
            if (i == 0) {
                for (Distributor distributor : distributors.values()) {
                    instanceProcedures.chooseProducers(distributor, producerList);
                }
            }

            if (i != 0) {
                check = 0;

                for (Distributor distributor : distributors.values()) {
                    if (distributor.isBankrupt()) {
                        check++;
                    }
                }

                if (check == distributors.values().size()) {
                    break;
                }

                updater.updateData(clients, distributors, updates, i);
                contractActions.clientBankruptcy(distributors, clients);
            }

            finance.updatePrices(distributors, prices, i);
            finance.clientsGetSalaries(clients);
            contractActions.distributorBankruptcy(distributors, clients);
            contractActions.clientContractsUpdate(distributors, clients, prices);
            finance.clientPaysBills(clients, distributors);
            finance.distributorsBudgetUpdates(distributors);
            contractActions.clientBankruptcy(distributors, clients);

            if (i != 0) {
                updater.updateProducers(producers, updates, i);
                instanceProcedures.newDistributorProducers(distributors, producerList);
                instanceProcedures.addMonthlyStats(producers, i);
            }
        }
    }
}
