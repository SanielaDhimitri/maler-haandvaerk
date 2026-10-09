package app;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmailConcurrencyTest {

    @Test
    void fiveCustomersCreateWorkRequestsAtSameTime() throws Exception {

        // 1. ExecutorService - thread pool med 5 threads
        ExecutorService executor =
                Executors.newFixedThreadPool(5);


        // 2. Callable tasks - 5 forskellige kunder

        Callable<String> customer1 = () ->
                createWorkRequest(
                        1,
                        "Anna",
                        "Jensen",
                        "sadh1000@stud.ek.dk",
                        "12345678",
                        "Jægersborgvej 10",
                        "Jeg vil gerne have malet min stue."
                );

        Callable<String> customer2 = () ->
                createWorkRequest(
                        2,
                        "Peter",
                        "Hansen",
                        "sadh1000@stud.ek.dk",
                        "12345678",
                        "Lyngbyvej 20",
                        "Jeg vil gerne have malet mit soveværelse."
                );

        Callable<String> customer3 = () ->
                createWorkRequest(
                        3,
                        "Maria",
                        "Nielsen",
                        "sadh1000@stud.ek.dk",
                        "12345678",
                        "Gentoftegade 30",
                        "Jeg har brug for hjælp til VVS."
                );

        Callable<String> customer4 = () ->
                createWorkRequest(
                        4,
                        "Mikkel",
                        "Larsen",
                        "sadh1000@stud.ek.dk",
                        "12345678",
                        "Ordrupvej 40",
                        "Jeg vil gerne have slebet mit gulv."
                );

        Callable<String> customer5 = () ->
                createWorkRequest(
                        5,
                        "Sofie",
                        "Andersen",
                        "sadh1000@stud.ek.dk",
                        "12345678",
                        "Bernstorffsvej 50",
                        "Jeg vil gerne have monteret et skab."
                );


        // 3. Submit alle Callable tasks
        List<Future<String>> futures =
                executor.invokeAll(
                        List.of(
                                customer1,
                                customer2,
                                customer3,
                                customer4,
                                customer5
                        )
                );


        // 4. Hent resultater fra Future
        for (Future<String> future : futures) {

            String result = future.get();

            System.out.println(result);
            System.out.println("-------------------------");
        }


        // 5. Luk ExecutorService
        executor.shutdown();
    }


    private String createWorkRequest(
            int customerNumber,
            String fornavn,
            String efternavn,
            String email,
            String telefon,
            String adresse,
            String beskrivelse
    ) throws Exception {

        HttpClient client =
                HttpClient.newHttpClient();

        String json = """
                {
                  "fornavn": "%s",
                  "efternavn": "%s",
                  "email": "%s",
                  "telefon": "%s",
                  "adresse": "%s",
                  "beskrivelse": "%s"
                }
                """.formatted(
                fornavn,
                efternavn,
                email,
                telefon,
                adresse,
                beskrivelse
        );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "http://localhost:7072/api/workrequests"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(json)
                        )
                        .build();


        System.out.println(
                "Kunde " + customerNumber
                        + " sender arbejdsforespørgsel"
        );


        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        return "Kunde "
                + customerNumber
                + " | HTTP "
                + response.statusCode()
                + "\n"
                + response.body();
    }
}