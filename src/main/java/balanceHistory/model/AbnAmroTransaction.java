package balanceHistory.model;

import java.util.Date;
import java.util.Objects;

public class AbnAmroTransaction extends BankTransaction {
    private String currency;
    private float beforeBalance;
    private float afterBalance;
    private Date otherDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbnAmroTransaction)) return false;
        if (!super.equals(o)) return false;
        AbnAmroTransaction that = (AbnAmroTransaction) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getBankAccountFrom(), that.getBankAccountFrom()) &&
                Objects.equals(getBankAccountTo(), that.getBankAccountTo()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Float.compare(that.getBeforeBalance(), getBeforeBalance()) == 0 &&
                Float.compare(that.getAfterBalance(), getAfterBalance()) == 0 &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getOtherDate(), that.getOtherDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getBankAccountFrom(), getAmount(), getAfterBalance());
    }

    @Override
    public String toString() {
        return "AbnAmroTransaction{" +
                "currency='" + currency + '\'' +
                ", beforeBalance=" + beforeBalance +
                ", afterBalance=" + afterBalance +
                ", otherDate=" + otherDate +
                "} " + super.toString();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(float beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public float getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(float afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Date getOtherDate() {
        return otherDate;
    }

    public void setOtherDate(Date otherDate) {
        this.otherDate = otherDate;
    }
}
