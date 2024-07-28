package common;

import financial.DistributorPrice;
import instances.Producer;

/**
 * Clasa folosita pentru salvarea comparatorilor.
 * @author Dutu Alin Calin
 */
public final class Comparators {
    /**
     * Acest comparator ajuta la sortarea producatorilor dupa tipul de energie prioritizandu-i
     * pe cei cu energie regenerabila. Daca sunt 2 producatori cu acelasi tip de energie, acestia
     * se vor compara dupa pret.
     * @param producer1 Primul producator;
     * @param producer2 Al doilea producator;
     * @return Returneaza 1 daca primul are energie neregenerabila si al doilea are regenerabila,
     * -1 daca primul are regenerabila si al doilea neregenerabila si rezultatul functiei
     *  sortProducerPrice in caz de egalitate.
     */
    public int sortProducerType(final Producer producer1, final Producer producer2) {
        if (producer1.renewableEnergy() > producer2.renewableEnergy()) {
            return -1;
        }

        if (producer1.renewableEnergy() < producer2.renewableEnergy()) {
            return 1;
        }

        return (sortProducerPrice(producer1, producer2));
    }

    /**
     * Acest comparator ajuta la sortarea producatorilor dupa pretul per KW prioritizandu-i
     * pe cei cu pretul mai mic. Daca sunt 2 producatori cu acelasi pret, acestia se vor
     * compara dupa cantitate.
     * @param producer1 Primul producator;
     * @param producer2 Al doilea producator;
     * @return Returneaza 1 daca al doilea producator are pretul mai mic,
     * -1 daca primul producator are pretul mai mic si rezultatul functiei
     * sortProducerQuantity in caz de egalitate.
     */
    public int sortProducerPrice(final Producer producer1, final Producer producer2) {
        if (producer1.getPriceKW() > producer2.getPriceKW()) {
            return 1;
        }

        if (producer1.getPriceKW() < producer2.getPriceKW()) {
            return -1;
        }

        return (sortProducerQuantity(producer1, producer2));
    }

    /**
     * Acest comparator ajuta la sortarea producatorilor dupa cantitate prioritizandu-i
     * pe cei cu cantitatea mai mare. Daca sunt 2 producatori cu aceeasi cantitate, acestia se vor
     * compara dupa id.
     * @param producer1 Primul producator;
     * @param producer2 Al doilea producator;
     * @return Returneaza 1 daca al doilea producator are cantitate mai mare, -1 daca primul
     * producator are cantitatea mai mare si rezultatul functiei sortProducerId in caz de egalitate.
     */
    public int sortProducerQuantity(final Producer producer1, final Producer producer2) {
        if (producer1.getEnergyPerDistributor() < producer2.getEnergyPerDistributor()) {
            return 1;
        }

        if (producer1.getEnergyPerDistributor() > producer2.getEnergyPerDistributor()) {
            return -1;
        }

        return sortProducerId(producer1, producer2);
    }

    /**
     * Acest comparator ajuta la sortarea producatorilor dupa id in ordine crescatoare.
     * @param producer1 Primul producator;
     * @param producer2 Al doilea producator;
     * @return Returneaza 1 daca al primul producator are id-ul mai mare si -1 daca al doilea
     * producator are id-ul mai mare.
     */
    public int sortProducerId(final Producer producer1, final Producer producer2) {
        return Integer.compare(producer1.getId(), producer2.getId());

    }

    /**
     * Acest comparator va sorta crescator toate elementele.
     *
     * @param price1 Primul pret;
     * @param price2 Al doilea pret;
     * @return Va returna 1 daca elementele sunt descrescatoare, -1 daca sunt crescatoare
     * si 0  daca sunt egale.
     */
    public int sortPrice(final DistributorPrice price1, final DistributorPrice price2) {
        return Integer.compare(price1.getPrice(), price2.getPrice());
    }

    /**
     * Acest comparator ajuta la sortarea preturilor dupa id-ul distribuitorului care ofera
     * acel pret, in ordine crescatoare.
     * @param price1 Primul pret;
     * @param price2 Al doilea pret;
     * @return Returneaza 1 daca al primul pret are id-ul distribuitorului mai mare si -1 daca
     * al doilea pret are id-ul distribuitorului mai mare.
     */
    public int sortPricesId(final DistributorPrice price1, final DistributorPrice price2) {
        if (price1.getId() > price2.getId()) {
            return 1;
        }

        return -1;
    }

    /**
     * Aceasta functie ajuta la sortarea id-urilor distribuitorilor in ordine crescatoare.
     * @param id1 Primul id;
     * @param id2 Al doilea id;
     * @return Se returneaza 1 daca primul id este mai mare si -1 in caz contrar.
     */
    public int sortDistributorIdsAsc(final int id1, final int id2) {
        if (id1 > id2) {
            return 1;
        }

        return -1;
    }
}
