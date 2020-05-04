package testNewProject.service;

import testNewProject.model.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionLoader {


    public ArrayList<BankTransaction> loadTransactions(String dir) {
        ArrayList<BankTransaction> bankTransactions = new ArrayList<>();

        bankTransactions.addAll(loadIngTransactions(dir));
        bankTransactions.addAll(loadAbnAmroTransactions(dir));
        bankTransactions = sortTransaction(bankTransactions);
        return bankTransactions;
    }

    private ArrayList<BankTransaction> sortTransaction(ArrayList<BankTransaction> transactions) {
        List<BankTransaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(BankTransaction::getDate))
                .collect(Collectors.toList());

        for (BankTransaction transaction : sortedTransactions) {
            System.out.println(transaction);
        }
        return (ArrayList<BankTransaction>) sortedTransactions;
    }

    private List<IngTransaction> loadIngTransactions(String dir) {
        List<String> transactionFilesPaths = getFilePaths(dir, ".csv");
        List<List<String>> allRawTransactions = getAllRawTransactions(transactionFilesPaths, 1, ';', '"');
        List<IngTransaction> ingTransactions = createIngBankTransactions(allRawTransactions, Bank.ING);
        ingTransactions = createIngBankSpaarTransactions(ingTransactions);
        return ingTransactions;
    }

    private List<IngTransaction> createIngBankSpaarTransactions(List<IngTransaction> ingTransactions) {
        ArrayList<IngTransaction> newTransactions = new ArrayList<>();

        for (IngTransaction transaction : ingTransactions) {
            if (transaction.getDescription().contains("Oranje spaarrekening")) {
                IngTransaction newTransaction = new IngTransaction();
                newTransaction.setDate(transaction.getDate());
                newTransaction.setAccount(transaction.getAccount() + AppConstants.SPAAR_SUFFIX);
                newTransaction.setAmount(transaction.getAmount() * (-1));
                newTransaction.setDescription(transaction.getDescription());
                newTransaction.setId();
                newTransactions.add(newTransaction);
            }
        }
        ingTransactions.addAll(newTransactions);
        return ingTransactions;
    }

    private List<AbnAmroTransaction> loadAbnAmroTransactions(String dir) {
        List<String> transactionFilesPaths = getFilePaths(dir, ".TAB");
        List<List<String>> allTransactions = getAllRawTransactions(transactionFilesPaths, 0, '\t', null);
        List<AbnAmroTransaction> abnAmroTransactions = createAbnAmroBankTransactions(allTransactions, Bank.ABN);
        return abnAmroTransactions;
    }

    private List<IngTransaction> createIngBankTransactions(List<List<String>> allTransactions, Bank bank) {
        ArrayList<IngTransaction> ingTransactions = new ArrayList<>();

        for (List<String> transactionDetails : allTransactions) {

            IngTransaction transaction = new IngTransaction();

            System.out.println( "Transaction Details size:" + transactionDetails.size() );
            System.out.println( transactionDetails );
            try {
                transaction.setDate(new SimpleDateFormat("yyyyMMdd").parse(transactionDetails.get(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.setNote1(transactionDetails.get(1));
            transaction.setAccount(transactionDetails.get(2));
            transaction.setMutationTypeCode(transactionDetails.get(3));
            transaction.setOppositeAccount(transactionDetails.get(4));
            transaction.setSign(transactionDetails.get(5));

            String amount = transactionDetails.get(6).replace(",", ".");
            transaction.setAmount(transaction.getSign().equals("Af") ? Float.valueOf(amount) * -1 : Float.valueOf(amount));

            transaction.setMutationType(transactionDetails.get(7));
            transaction.setNote2(transactionDetails.get(8));
            transaction.setDescription(transaction.getNote1() + "; " + transaction.getNote2());

            transaction.setId();
//            System.out.println(transaction);

            ingTransactions.add(transaction);
        }

        return ingTransactions;
    }

    private List<AbnAmroTransaction> createAbnAmroBankTransactions(List<List<String>> allTransactions, Bank bank) {
        ArrayList<AbnAmroTransaction> abnAmroTransactions = new ArrayList<>();

        for (List<String> transactionDetails : allTransactions) {

            AbnAmroTransaction transaction = new AbnAmroTransaction();
            transaction.setAccount(transactionDetails.get(0));
            transaction.setCurrency(transactionDetails.get(1));
            try {
                transaction.setDate(new SimpleDateFormat("yyyyMMdd").parse(transactionDetails.get(2)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.setBeforeBalance(Float.valueOf(transactionDetails.get(3).replace(",", ".")));
            transaction.setAfterBalance(Float.valueOf(transactionDetails.get(4).replace(",", ".")));
            //some sort of date at 5? transaction.setCurrency(transactionDetails.get(5));
            transaction.setAmount(Float.valueOf(transactionDetails.get(6).replace(",", ".")));
            transaction.setDescription(transactionDetails.get(7));
            transaction.setCurrency(transactionDetails.get(7));

            transaction.setId();
//            System.out.println(transaction);

            abnAmroTransactions.add(transaction);
        }

        return abnAmroTransactions;
    }

    private List<String> getFilePaths(String dir, String suffix) {

        List<String> pathList = new ArrayList<>();

        File[] files = new File(dir).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(suffix)) {
                pathList.add(dir + "\\" + file.getName());
            }
        }

        return pathList;
    }

    private List<List<String>> getAllRawTransactions(List<String> filesPaths, int skipLines, Character delimiter, Character quote) {

        List<List<String>> allRawTransactions = new ArrayList<>();

        // Loop over all files
        for (String filePath : filesPaths) {
            List<List<String>> rawTransactions = new ArrayList<>();

            rawTransactions.addAll(getTransactionsFromFile(filePath, skipLines, delimiter, quote));

            // Add file's list of string to all list of strings
            allRawTransactions.addAll(rawTransactions);
        }

        return allRawTransactions;
    }

    private List<List<String>> getTransactionsFromFile(String filePath, int skipLines, Character delimiter, Character quote) {

        int linesRead = 0;

        List<List<String>> fileTransactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (skipLines != 0 && linesRead == 0) {
                    linesRead++;
                    continue;
                }
                //clean data
                line = cleanseData(line, delimiter, quote);
                String[] transactionValues = line.split(String.valueOf(delimiter));
                fileTransactions.add(Arrays.asList(transactionValues));
                linesRead++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileTransactions;
    }

    private String cleanseData(String file, Character delimiter, Character quote) {

        String copy = "";

        boolean inQuotes = false;

        for (int i = 0; i < file.length(); ++i) {
            if (quote != null && file.charAt(i) == quote)   // Entering/exiting quoted String
                inQuotes = !inQuotes;
            if (file.charAt(i) == delimiter && inQuotes)    //Replace delimiter within quotes with custom value
                copy += '.';
            else
                copy += file.charAt(i);                     // Otherwise just copy character
        }


        copy = copy.replace(String.valueOf(quote), "");

//        System.out.println(file);
//        System.out.println(copy);


        return copy;
    }

}