package balanceHistory.model;

import java.util.Date;
import java.util.Objects;

public class BankTransaction {

    private int id;
    private Date date;
    private BankAccount bankAccountFrom;
    private BankAccount bankAccountTo;
    private Float amount;
    private String description;

    private String rawIban;


    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", date=" + date +
                ", accountFrom=" + Objects.toString(bankAccountFrom.getIban()) +
                ", accountTo=" + Objects.toString(bankAccountTo.getIban())  +
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
                Objects.equals(getBankAccountFrom(), that.getBankAccountFrom()) &&
                Objects.equals(getBankAccountTo(), that.getBankAccountTo()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    /**
     * @return BankAccount return the bankAccountFrom
     */
    public BankAccount getBankAccountFrom() {
        return bankAccountFrom;
    }

    /**
     * @param bankAccountFrom the bankAccountFrom to set
     */
    public void setBankAccountFrom(BankAccount bankAccountFrom) {
        this.bankAccountFrom = bankAccountFrom;
    }

    /**
     * @return BankAccount return the bankAccountTo
     */
    public BankAccount getBankAccountTo() {
        return bankAccountTo;
    }

    /**
     * @param bankAccountTo the bankAccountTo to set
     */
    public void setBankAccountTo(BankAccount bankAccountTo) {
        this.bankAccountTo = bankAccountTo;
    }


    /**
     * @return String return the rawIban
     */
    public String getRawIban() {
        return rawIban;
    }

    /**
     * @param rawIban the rawIban to set
     */
    public void setRawIban(String rawIban) {
        this.rawIban = rawIban;
    }

}
