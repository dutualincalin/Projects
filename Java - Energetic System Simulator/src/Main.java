import system.SystemRounds;
import common.Comparators;
import common.Constants;
import financial.DistributorPrice;
import input.JacksonToDataConverter;
import input.LoaderIn;
import instances.Client;
import instances.Distributor;
import instances.MonthUpdate;
import instances.Producer;
import iofactory.IO;
import iofactory.IOFactory;
import output.DataToJacksonConverter;
import output.LoaderOut;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Aceasta clasa are rolul de a rula programul.
 * @author Dutu Alin Calin
 */
public final class Main {
    private Main() { }

    /**
     * Aceasta functie este centrul de comanda al programului. Aici se vor apela functiile pentru
     * preluarea datelor dintr-un fisier JSON, prelucrarea acestora pentru un scop bine definit si
     * pentru scrierea rezultatului intr-un fisier de output.
     * @param args Primul argument contine calea catre fisierul de input, iar al doilea argument
     *             are calea catre fisierul de output;
     * @throws Exception I se spune compilatorului ca nu este o problema daca programul arunca
     * Exception.
     */
    public static void main(final String[] args) throws Exception {
        String inPath = args[0];

        IO input = IOFactory.createIO(Constants.INPUT, inPath);
        input.execute();
        LoaderIn loaderIn = LoaderIn.getLoader();

        JacksonToDataConverter populate = new JacksonToDataConverter(loaderIn);
        DataToJacksonConverter depopulate = new DataToJacksonConverter();
        populate.populate();

        Comparators comparator = new Comparators();
        SystemRounds system = new SystemRounds();
        LinkedHashMap<Integer, Client> clients = populate.getClients();
        LinkedHashMap<Integer, Distributor> distributors = populate.getDistributors();
        LinkedHashMap<Integer, Producer> producers = populate.getProducers();
        ArrayList<Producer> producerList = new ArrayList<>(producers.values());
        ArrayList<MonthUpdate> updates = populate.getUpdates();
        ArrayList<DistributorPrice> prices = new ArrayList<>();

        system.systemExecute(populate.getMonths(), clients, distributors, producers,
                producerList, updates, prices);


        prices.sort(comparator :: sortPricesId);
        LoaderOut.setLoader(depopulate.loader(clients, distributors, producers, prices));
        String outPath = args[1];
        IO output = IOFactory.createIO(Constants.OUTPUT, outPath);
        output.execute();
    }
}
