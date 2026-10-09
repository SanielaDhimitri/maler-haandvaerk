package app.controllers;

import app.concurrency.ChatTask;
import app.dto.ChatRequestDTO;
import app.services.ChatService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChatController {

    private final ChatService chatService;

    // Thread pool med plads til 10 samtidige chat-opgaver
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(10);

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Sender et spørgsmål til chatten
    public void ask(Context ctx) {

        ChatRequestDTO request =
                ctx.bodyValidator(ChatRequestDTO.class)

                        .check(
                                r -> r.question() != null,
                                "Question skal angives"
                        )

                        .check(
                                r -> !r.question().isBlank(),
                                "Question må ikke være tom"
                        )

                        .get();

        try {

            ChatTask task =
                    new ChatTask(
                            chatService,
                            request.question()
                    );

            Future<String> future =
                    executorService.submit(task);

            String answer =
                    future.get();

            ctx.status(HttpStatus.OK)
                    .json(Map.of(
                            "answer", answer
                    ));

        } catch (Exception e) {

            throw new RuntimeException(
                    "Fejl under behandling af chat-spørgsmål",
                    e
            );
        }
    }
}