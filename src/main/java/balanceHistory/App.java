package balanceHistory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import balanceHistory.model.*;
import balanceHistory.service.*;


public class App {
    public static void main(String[] args) throws GeneralSecurityException, IOException {


        // Start with where all data and configuration resides
        PathSelector pathSelector = new PathSelector();
        AppConstants.FILES_PATH = pathSelector.selectPath();

        // Load new bank accounts from json file
        Accountant accountant = new Accountant();
        accountant.makeNewBankAccounts();

        // Set 01-01-2018 balances + make dummy transactions for those
        ArrayList<BankTransaction> dummyTransactions = accountant.createDummyTransactions();

        // Load all transactions
        TransactionLoader transactionLoader = new TransactionLoader();
        ArrayList<BankTransaction> bankTransactions = transactionLoader.loadTransactions(AppConstants.FILES_PATH);

        // Provide the regulator all the transactions
        Regulator regulator = new Regulator();
        regulator.addBankTransactions(dummyTransactions);
        regulator.addBankTransactions(bankTransactions);
        regulator.defineBankAccounts();

        System.out.println("Amount of bank accounts from accountant: " + accountant.getBankAccounts().size());
        System.out.println("Amount of bank transactions loaded: " + bankTransactions.size());
        System.out.println("Amount of bank accounts from regulator: " + regulator.getBankAccounts().size());
        System.out.println("Amount of bank transactions from regulator: " + regulator.getBankTransactions().size());


        // Calculate balances
        ArrayList<Balance> balances = accountant.calculateBalances(regulator.getBankTransactions());
        System.out.println("Amount of balances from accountant: " + balances.size());

        // Update Google Spreadsheet
        regulator.uploadBalances(balances);
        regulator.uploadTransactions(regulator.getBankTransactions());
        regulator.updateLog();
    }
}
