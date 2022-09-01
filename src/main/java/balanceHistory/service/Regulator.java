package balanceHistory.service;

import javax.swing.*;

import balanceHistory.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Regulator {

    private List<BankAccount> bankAccounts = new ArrayList<>();
    private List<BankTransaction> bankTransactions = new ArrayList<>();

        public void addBankTransactions(Accountant accountant, ArrayList<BankTransaction> bankTransactions) {

        // deduplicate
        List<BankTransaction> bankTransactions1 = bankTransactions.stream()
                .collect(Collectors.collectingAndThen(
                        (Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(BankTransaction::getId)))),
                        ArrayList::new));

         // Get info only the accountant has
        for (BankTransaction transaction : bankTransactions1) {
            BankAccount bankAccountFromJSON = accountant.getBankAccountByIban(transaction.getAccount());
            if (null != bankAccountFromJSON) {
                transaction.setAccountOwner(bankAccountFromJSON.getOwner());
                transaction.setAccountType(bankAccountFromJSON.getType());
            }
        }

        // set
        this.bankTransactions.addAll(bankTransactions1);
    }

    public void defineBankAccounts(Accountant accountant) {
        for (BankTransaction transaction : this.bankTransactions) {
            if (isNewBankAccount(transaction.getAccount())) {
                BankAccount bankAccount = new BankAccount();
               
                // bank basics
                bankAccount.setIban(transaction.getAccount());

                // Get info only the accountant has
                BankAccount bankAccountFromJSON = accountant.getBankAccountByIban(transaction.getAccount());
                if (null != bankAccountFromJSON) {
                    bankAccount.setOwner(bankAccountFromJSON.getOwner());
                    bankAccount.setType(bankAccountFromJSON.getType());
                }

                // set bank type based on transaction info
                if (transaction instanceof AbnAmroTransaction) {
                    bankAccount.setBank(Bank.ABN);
                } else {
                    bankAccount.setBank(Bank.ING);
                }
                
                // set balance via pop-up window (deprecated)
                // bankAccount.setStartBalance(askUserInput(bankAccount.getIban()));

                this.bankAccounts.add(bankAccount);
            }
        }
    }

    private boolean isNewBankAccount(String accountNumber) {

        if (this.bankAccounts.size() == 0) return true;

        boolean foundBankAccount = false;
        for (BankAccount bankAccount : this.bankAccounts) {
            if (bankAccount.getIban().equals(accountNumber)) foundBankAccount = true;
        }
        return !foundBankAccount;
    }

    private Float askUserInput(String iban) {
        JTextField xField = new JTextField(8);
        JTextField yField = new JTextField(25);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Current balance + Account holder:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(2)); // a spacer
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Info required for: " + iban, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("iban: " + iban);
            System.out.println("balance: " + xField.getText());
            System.out.println("description: " + yField.getText());
        }

        return Float.valueOf(xField.getText());
    }

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
        header.add("Datum");
        header.add("Balans");
        header.add("Verschil");
        input.add(header);

        for (Balance balance : balances) {
            List<Object> balanceEntry = new ArrayList<>();
            balanceEntry.add(balance.getAccountOwner());
            balanceEntry.add(balance.getAccountType());
            balanceEntry.add(new SimpleDateFormat("yyyyMMdd").format(balance.getDate()));
            balanceEntry.add(balance.getBalance().doubleValue());
            balanceEntry.add(balance.getDifference().doubleValue());
            input.add(balanceEntry);
        }

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("Balans!A1:E", input);
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
            List<Object> balanceEntry = new ArrayList<>();
            balanceEntry.add(transaction.getAccountOwner());
            balanceEntry.add(transaction.getAccountType());
            balanceEntry.add(new SimpleDateFormat("yyyyMMdd").format(transaction.getDate()));
            balanceEntry.add(transaction.getAmount().doubleValue());
            input.add(balanceEntry);
        }

        GoogleUpdater googleUpdater = new GoogleUpdater();
        googleUpdater.updateValues("Transactie!A1:D", input);
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
