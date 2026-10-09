package app.concurrency;

import app.services.ChatService;

import java.util.concurrent.Callable;

public class ChatTask implements Callable<String> {

    private final ChatService chatService;
    private final String question;

    public ChatTask(ChatService chatService, String question) {
        this.chatService = chatService;
        this.question = question;
    }

    @Override
    public String call() {

        System.out.println(
                Thread.currentThread().getName()
                        + " behandler chat-spørgsmål: "
                        + question
        );

        return chatService.ask(question);
    }
}