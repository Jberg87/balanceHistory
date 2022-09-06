package balanceHistory.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import balanceHistory.model.BankAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {

    public String readProperty( String filePath, String jsonObjectName, String property) {

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader( new File (filePath)));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject projectSettings = (JSONObject) jsonObject.get(jsonObjectName);

            System.out.println(projectSettings.get(property));
            return (String) projectSettings.get(property);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "No property found with this input property: " + property;
    }

    public ArrayList<BankAccount> readBankAccounts(String filePath) {

        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(new File(filePath)));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("accounts");

            jsonArray.forEach( jsonObj -> {
                BankAccount bankAccount = new BankAccount();

                JSONObject jsonAccount = (JSONObject) jsonObj;
                bankAccount.setBank( (String) jsonAccount.get( "bank"));
                bankAccount.setIban( (String) jsonAccount.get( "iban"));
                bankAccount.setOwner( (String) jsonAccount.get( "owner"));
                bankAccount.setType( (String) jsonAccount.get( "type"));
                bankAccount.setDescription( (String) jsonAccount.get( "description"));
                bankAccount.setOffset( Float.parseFloat ( (String) jsonAccount.get("offset")) );
                bankAccount.setBasisIban( (String) jsonAccount.get( "basis_iban"));

                bankAccounts.add(bankAccount);
            } );

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bankAccounts;
    }
}
