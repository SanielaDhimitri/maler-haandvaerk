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

public class ChatConcurrencyTest {

    @Test
    void fiveCustomersUseChatAtSameTime() throws Exception {

        // 1. ExecutorService - thread pool med 5 threads
        ExecutorService executor =
                Executors.newFixedThreadPool(5);


        // 2. Callable tasks - 5 forskellige kunder

        Callable<String> customer1 = () ->
                sendQuestion(
                        1,
                        "Hvad koster det at male min lejlighed?"
                );

        Callable<String> customer2 = () ->
                sendQuestion(
                        2,
                        "Kan I male både vægge og loft?"
                );

        Callable<String> customer3 = () ->
                sendQuestion(
                        3,
                        "Kan I hjælpe med VVS arbejde?"
                );

        Callable<String> customer4 = () ->
                sendQuestion(
                        4,
                        "Hvordan booker jeg en tid?"
                );

        Callable<String> customer5 = () ->
                sendQuestion(
                        5,
                        "Kan I hjælpe med elektrikerarbejde?"
                );


        // 3. Submit alle Callable tasks
        // invokeAll returnerer List<Future<String>>

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


        // 4. Hent resultaterne fra Future

        for (Future<String> future : futures) {

            String result = future.get();

            System.out.println(result);
            System.out.println("-------------------------");
        }


        // 5. Luk ExecutorService

        executor.shutdown();
    }


    private String sendQuestion(
            int customerNumber,
            String question
    ) throws Exception {

        HttpClient client =
                HttpClient.newHttpClient();

        String json = """
                {
                  "question": "%s"
                }
                """.formatted(question);


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "http://localhost:7072/api/chat/"
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
                        + " sender: "
                        + question
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