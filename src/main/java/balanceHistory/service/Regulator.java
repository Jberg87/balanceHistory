package balanceHistory.service;

import balanceHistory.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Regulator {

    private List<BankAccount> bankAccounts = new ArrayList<>();
    private List<BankTransaction> bankTransactions = new ArrayList<>();

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public List<BankTransaction> getBankTransactions() {
        return bankTransactions;
    }

    public void uploadBalances(ArrayList<Balance> balances) throws GeneralSecurityException, IOException {
        List<List<Object>> input = new ArrayList<>();

        List<Object> header = new ArrayList<>();
        header.add("Eigenaar");
        header.add("Type");
        header.add("iban");
        header.add("Datum");
        header.add("Balans");
        header.add("Verschil");
        input.add(header);

        for (Balance balance : balances) {
            List<Object> balanceEntry = new ArrayList<>();
            balanceEntry.add(balance.getAccount().getOwner());
            balanceEntry.add(balance.getAccount().getType());
            balanceEntry.add(balance.getAccount().getIban());
            balanceEntry.add(new SimpleDateFormat("yyyyMMdd").format(balance.getDate()));
            balanceEntry.add(balance.getBalance().doubleValue());
            balanceEntry.add(balance.getDifference().doubleValue());
            input.add(balanceEntry);
        }

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("Balans!A1:F", input);
    }

    public void uploadTransactions(List<BankTransaction> bankTransactions) throws GeneralSecurityException, IOException {
        List<List<Object>> input = new ArrayList<>();

        List<Object> header = new ArrayList<>();
        header.add("Eigenaar");
        header.add("Type");
        header.add("Datum");
        header.add("Hoeveelheid");
        input.add(header);

        for (BankTransaction transaction : bankTransactions) {
            // Do not load any kind of savings transactions
            String bankAccountTypes = "";
            try {
                bankAccountTypes += Objects.toString(transaction.getBankAccountFrom().getType(), "");
            } catch (NullPointerException err) {
            }
            try {
                bankAccountTypes += Objects.toString(transaction.getBankAccountTo().getType(), "");
            } catch (NullPointerException err) {
            }
            if (!bankAccountTypes.contains("Spaar") ) {
                List<Object> balanceEntry = new ArrayList<>();
                if (null == transaction.getBankAccountFrom()) {
                    balanceEntry.add("onbekend");
                    balanceEntry.add("onbekend");
                } else {
                    balanceEntry.add(transaction.getBankAccountFrom().getIban());
                    balanceEntry.add(transaction.getBankAccountFrom().getType());
                }
                balanceEntry.add(new SimpleDateFormat("yyyyMMdd").format(transaction.getDate()));
                balanceEntry.add(transaction.getAmount().doubleValue());
                input.add(balanceEntry);
            }
        }

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("Transactie!A1:D", input);
    }

    public void uploadRawTransactions(List<BankTransaction> bankTransactions) throws GeneralSecurityException, IOException {
        List<List<Object>> input = new ArrayList<>();

        List<Object> header = new ArrayList<>();
        header.add("RawIban");
        header.add("EigenaarVan");
        header.add("TypeVan");
        header.add("EigenaarNaar");
        header.add("TypeNaar");
        header.add("Datum");
        header.add("Hoeveelheid");
        header.add("Beschrijving");
        input.add(header);

        for (BankTransaction transaction : bankTransactions) {
            List<Object> balanceEntry = new ArrayList<>();
            balanceEntry.add(Objects.toString(transaction.getRawIban(), "onbekend"));
            if (null == transaction.getBankAccountFrom()) {
                balanceEntry.add("onbekend");
                balanceEntry.add("onbekend");
            } else {
                balanceEntry.add(transaction.getBankAccountFrom().getIban());
                balanceEntry.add(transaction.getBankAccountFrom().getType());
            }
            if (null == transaction.getBankAccountTo()) {
                balanceEntry.add("onbekend");
                balanceEntry.add("onbekend");
            } else {
                balanceEntry.add(transaction.getBankAccountTo().getIban());
                balanceEntry.add(transaction.getBankAccountTo().getType());
            }
            balanceEntry.add(new SimpleDateFormat("yyyyMMdd").format(transaction.getDate()));
            balanceEntry.add(transaction.getAmount().doubleValue());
            balanceEntry.add(transaction.getDescription());
            input.add(balanceEntry);
        }

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("RawTransactie!A1:H", input);
    }

    public void updateLog() throws GeneralSecurityException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);

        List<List<Object>> input = new ArrayList<>();
        List<Object> header = new ArrayList<>();
        header.add("Last update:");
        header.add(strDate);
        input.add(header);

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("Log!A1:B", input);
    }
}
