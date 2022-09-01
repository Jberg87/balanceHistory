package balanceHistory.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import balanceHistory.model.Balance;
import balanceHistory.model.BankAccount;
import balanceHistory.model.BankTransaction;

public class Accountant {

    ArrayList<BankAccount> bankAccounts = new ArrayList<>();

    public void makeNewBankAccounts() {
        JsonParser jsonParser = new JsonParser();
        bankAccounts.addAll(jsonParser.readBankAccounts(AppConstants.FILES_PATH + "\\accounts.json"));
    }

    public ArrayList<BankTransaction> createDummyTransactions() {
        ArrayList<BankTransaction> dummyTransactions = new ArrayList<>();

        for (BankAccount bankAccount : this.bankAccounts) {
            BankTransaction transaction = new BankTransaction();
            transaction.setAccount(bankAccount.getIban());
            transaction.setDescription("Use offset as starting balance");
            transaction.setAmount(bankAccount.getOffset());
            try {
                transaction.setDate(new SimpleDateFormat("yyyyMMdd").parse("20180101"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.setId();

            dummyTransactions.add(transaction);
        }

        return dummyTransactions;
    }

    public ArrayList<Balance> calculateBalances(List<BankTransaction> transactions) {

        ArrayList<Balance> balances = new ArrayList<>();

        for (BankAccount account : this.bankAccounts) {

            List<BankTransaction> accountTransactions = transactions.stream()
                    .filter(bankTransaction -> bankTransaction.getAccount().equals(account.getIban()))
                    .sorted(Comparator.comparing(BankTransaction::getDate))
                    .collect(Collectors.toList());

            boolean firstTransaction = true;

            Balance balance = new Balance();

            for (BankTransaction transaction : accountTransactions) {

                if (firstTransaction) { // first transaction
                    balance.setAccountOwner(account.getOwner());
                    balance.setAccountType(account.getType());
                    balance.setDate(transaction.getDate());
                    balance.setDifference(transaction.getAmount());
                    balance.setBalance(transaction.getAmount());

                    firstTransaction = false;

                    // new date so close old balance and make a new one for a new date
                } else if (!balance.getDate().toString().equals(transaction.getDate().toString())) { 

                    // Save previous balance
                    balances.add(balance);

                    // fill gap between dates with new balances
                    ArrayList<Balance> intermediateBalances = fillBalances(balance, transaction.getDate());
                    balances.addAll(intermediateBalances);

                    // Start new balance
                    Float tempBalance = balance.getBalance();
                    Float tempDifference = Float.valueOf("0.0");

                    balance = new Balance();
                    balance.setAccountOwner(account.getOwner());
                    balance.setAccountType(account.getType());
                    balance.setDate(transaction.getDate());
                    balance.setDifference(tempDifference + transaction.getAmount());
                    balance.setBalance(tempBalance + transaction.getAmount());

                } else { // Same day so only increase current balance
                    balance.setDifference(balance.getDifference() + transaction.getAmount());
                    balance.setBalance(balance.getBalance() + transaction.getAmount());
                }
            }
            // add last balance to balance list
            balances.add(balance);

            // Add missing balances up until run date
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = format.format(new Date());
            LocalDate tomorrowLocalDate = LocalDate.parse(todayString).plusDays(1);
            Date tomorrow = Date.from(tomorrowLocalDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            ArrayList<Balance> intermediateBalances = fillBalances(balance, tomorrow);
            balances.addAll(intermediateBalances);
        }

        return balances;
    }

    private ArrayList<Balance> fillBalances(Balance balance, Date stopDate) {

        ArrayList<Balance> balances = new ArrayList<>();

        // Check if there is more than 1 day of a difference between dates
        LocalDate balanceDate = (balance.getDate()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate transactionDate = stopDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Make a new balance date for the intermediate balances
        balanceDate = balanceDate.plusDays(1);

        while (!balanceDate.isEqual(transactionDate)) {
            // make new balance
            Balance newBalance = new Balance();
            newBalance.setAccountOwner(balance.getAccountOwner());
            newBalance.setAccountType(balance.getAccountType());

            // newBalance.setDate( Date.from(balanceDate.)
            // java.sql.Date.valueOf(balanceDate));
            newBalance.setDate(Date.from(balanceDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()));

            newBalance.setBalance(balance.getBalance());
            newBalance.setDifference((float) 0);
            balances.add(newBalance);

            balanceDate = balanceDate.plusDays(1);
        }

        return balances;
    }

    public BankAccount getBankAccountByIban(String iban) {
        for(BankAccount bankAccount : bankAccounts) {
            if(iban.equals(bankAccount.getIban()))
                return bankAccount;
        }

        return null;
    }

    public ArrayList<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
}
