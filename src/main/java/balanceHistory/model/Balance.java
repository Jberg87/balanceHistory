package balanceHistory.model;

import java.util.Date;

public class Balance {

    private Date date;
    private BankAccount account;
    private Float difference;
    private Float balance;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getDifference() {
        return difference;
    }

    public void setDifference(Float difference) {
        this.difference = difference;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    /**
     * @return BankAccount return the account
     */
    public BankAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(BankAccount account) {
        this.account = account;
    }

}
