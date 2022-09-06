package balanceHistory.model;

public class BankAccount {

    private String bank;
    private String iban;
    private String owner;
    private String type;
    private String description;
    private float offset;
    private String basisIban;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
    
    /**
     * @return String return the basisIban
     */
    public String getBasisIban() {
        return basisIban;
    }

    /**
     * @param basisIban the basisIban to set
     */
    public void setBasisIban(String basisIban) {
        this.basisIban = basisIban;
    }

}
