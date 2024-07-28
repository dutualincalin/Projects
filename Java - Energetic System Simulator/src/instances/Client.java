package instances;

import common.Constants;
import financial.Bill;
import financial.Contract;
import financial.Debt;

/**
 * Clasa folosita pentru prelucrarea datelor clientilor.
 * @author Dutu Alin Calin
 */
public final class Client {
    private int id;
    private int monthlyIncome;
    private int budget;
    private boolean isBankrupt;
    private Contract contract;
    private Debt debt;

    public Client(final int id, final int monthlyIncome, final int budget) {
        this.id = id;
        this.monthlyIncome = monthlyIncome;
        this.budget = budget;
        this.isBankrupt = false;
        contract = null;
        debt = null;
    }

    /**
     * Se inregistreaza un nou contract pe numele utilizatorului.
     * @param distributorId Id-ul distribuitorului;
     * @param price Rata stabilita in contract;
     * @param monthTime Durata in luni a contractului;
     */
    public void setContract(final int distributorId, final int price,
                            final int monthTime) {
        this.contract = new Contract(id, distributorId, price, monthTime);
    }

    /**
     * Se elimina contractul din informatiile clientului.
     */
    public void endContract() {
        contract = null;
    }

    /**
     * Clientul isi primeste salariul.
     */
    public void clientGetsSalary() {
        budget += monthlyIncome;
    }

    /**
     * In cazul in care clientul nu are penalizari, el va putea plati factura sau amana rata in
     * cazul in care nu mai are bani. In cazul in care clientul are penalitati, daca bugetul
     * clientului este suficient de mare, va plati factura actuala cu penalizarile lunii trecute,
     * altfel este considerat in faliment.
     * @return Se returneaza suma platita
     */
    public Bill pays() {
        int sum;

        if (debt == null) {
            if (budget >= contract.getPrice()) {
                budget -= contract.getPrice();
                return new Bill(contract.getDistributorId(), contract.getPrice());
            } else {
                debt = new Debt(contract.getDistributorId(), (int) Math.round(Math.floor(
                        Constants.PENALTY_NUMBER * contract.getPrice())));

                return null;
            }
        } else {
            if (budget >= contract.getPrice() + debt.getSum()) {
                sum = contract.getPrice() + debt.getSum();
                budget -= sum;
                debt = null;
                return new Bill(contract.getDistributorId(), sum);
            } else {
                if (contract.getDistributorId() != debt.getDistributorId()
                        && budget >= debt.getSum()) {

                    Bill bill = new Bill(debt.getDistributorId(), debt.getSum());
                    budget -= debt.getSum();
                    debt = null;
                    debt = new Debt(contract.getDistributorId(), (int) Math.round(Math.floor(
                            Constants.PENALTY_NUMBER * contract.getPrice())));
                    return bill;
                } else {
                    isBankrupt = true;
                    return null;
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public int getBudget() {
        return budget;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public Contract getContract() {
        return contract;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }
}
