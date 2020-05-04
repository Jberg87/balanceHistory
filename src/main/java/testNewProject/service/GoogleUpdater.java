package testNewProject.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleUpdater {


    private static String APPLICATION_NAME;
    private static String SPREADSHEET_ID;
    private static String USER_ID;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    private Sheets service;
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    private final String VALUE_INPUT_OPTION = "USER_ENTERED";

    public GoogleUpdater() throws GeneralSecurityException, IOException {

        JsonParser jsonParser = new JsonParser();
        APPLICATION_NAME = jsonParser.readProperty(AppConstants.FILES_PATH + "\\project_settings.json", "project_settings", "application_name");
        SPREADSHEET_ID = jsonParser.readProperty(AppConstants.FILES_PATH + "\\project_settings.json", "project_settings", "spreadsheet_id");
        USER_ID = jsonParser.readProperty(AppConstants.FILES_PATH + "\\project_settings.json", "project_settings", "user_id");

        this.service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(new File(AppConstants.FILES_PATH + "\\credentials.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER_ID); // user name of the Google API OAuth 2.0-client-ID
    }


    public UpdateValuesResponse updateValues(String range, List<List<Object>> values)
            throws IOException {
        Sheets service = this.service;

        ValueRange body = new ValueRange()
                .setValues(values);

        UpdateValuesResponse result =
                service.spreadsheets().values().update(SPREADSHEET_ID, range, body)
                        .setValueInputOption(VALUE_INPUT_OPTION)
                        .execute();

        System.out.printf("%d cells updated.", result.getUpdatedCells());

        return result;
    }

}


