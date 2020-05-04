package testNewProject.model;

import java.util.Objects;

public class IngTransaction extends BankTransaction {
    private String note1;
    private String oppositeAccount;
    private String mutationTypeCode;
    private String sign;
    private String mutationType;
    private String note2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngTransaction)) return false;
        if (!super.equals(o)) return false;
        IngTransaction that = (IngTransaction) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getNote1(), that.getNote1()) &&
                Objects.equals(getOppositeAccount(), that.getOppositeAccount()) &&
                Objects.equals(getMutationTypeCode(), that.getMutationTypeCode()) &&
                Objects.equals(getSign(), that.getSign()) &&
                Objects.equals(getMutationType(), that.getMutationType()) &&
                Objects.equals(getNote2(), that.getNote2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNote1(), getOppositeAccount(), getMutationTypeCode(), getSign(), getMutationType(), getNote2());
    }

    @Override
    public String toString() {
        return "IngTransaction{" +
                "note1='" + note1 + '\'' +
                ", oppositeAccount='" + oppositeAccount + '\'' +
                ", mutationTypeCode='" + mutationTypeCode + '\'' +
                ", sign='" + sign + '\'' +
                ", mutationType='" + mutationType + '\'' +
                ", note2='" + note2 + '\'' +
                "} " + super.toString();
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getOppositeAccount() {
        return oppositeAccount;
    }

    public void setOppositeAccount(String oppositeAccount) {
        this.oppositeAccount = oppositeAccount;
    }

    public String getMutationTypeCode() {
        return mutationTypeCode;
    }

    public void setMutationTypeCode(String mutationTypeCode) {
        this.mutationTypeCode = mutationTypeCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMutationType() {
        return mutationType;
    }

    public void setMutationType(String mutationType) {
        this.mutationType = mutationType;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }


}
