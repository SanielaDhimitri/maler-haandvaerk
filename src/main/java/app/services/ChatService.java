package app.services;

import app.api.GeminiApi;

public class ChatService {

    private final GeminiApi geminiApi;

    public ChatService(GeminiApi geminiApi) {
        this.geminiApi = geminiApi;
    }

    public String ask(String question) {
        return geminiApi.askGemini(question);
    }
}
//ChatService = håndterer chat-funktionen(gemini-key)
//ChatService modtager et spørgsmål fra ChatController og sender spørgsmålet videre til GeminiApi. Derefter returnerer den svaret fra Gemini.