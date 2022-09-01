package balanceHistory.model;

import java.util.Date;
import java.util.Objects;

public class BankTransaction {

    private int id;
    private Date date;
    private String account;
    private String accountOwner;
    private String accountType;
    private Float amount;
    private String description;


    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", date=" + date +
                ", account=" + account +
                ", accountOwner=" + accountOwner +
                ", accountType=" + accountType +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = hashCode();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String accountNumber) {
        this.account = accountNumber;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankTransaction)) return false;
        BankTransaction that = (BankTransaction) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash( getDate(), getAccount(), getAmount(), getDescription());
    }
}
