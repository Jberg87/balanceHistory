package testNewProject.model;

import java.util.Date;

public class Balance {

    private Date date;
    private String accountOwner;
    private String accountType;
    private Float difference;
    private Float balance;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
}
