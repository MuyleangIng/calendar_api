package com.example.idata.calendarapi;// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// [START calendar_quickstart]

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class CalendarService {
    private static final String APPLICATION_NAME = "idata";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/calendar.readonly");
    private static final String CREDENTIALS_FILE_PATH = "/home/sen/Desktop/spring-api/idata/src/main/resources/service-account.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public List<Event> getEvents() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        if (events == null || events.getItems() == null) {
            throw new IllegalStateException("No events found or received a null response from API.");
        }
        return events.getItems();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
        return credential;
    }
    public Event createEvent(Event eventDetails) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service.events().insert("primary", eventDetails).execute();
    }

//    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
//        System.out.println("Loading file: " + CREDENTIALS_FILE_PATH);
//        InputStream in = CalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in != null) {
//            // Read the contents of the file
//            Scanner scanner = new Scanner(in);
//            StringBuilder jsonContent = new StringBuilder();
//            while (scanner.hasNextLine()) {
//                jsonContent.append(scanner.nextLine());
//            }
//            scanner.close();
//            // Log the contents of the file
//            System.out.println("Service Account JSON Content:");
//            System.out.println(jsonContent.toString());
//            // Load the Google client secrets
//            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(jsonContent.toString()));
//            // Build the authorization flow
//            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                    .setAccessType("offline")
//                    .build();
//            // Authorize the user and return the credentials
//            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//        } else {
//            System.err.println("Failed to load service account JSON file.");
//            return null;
//        }
//    }

}
