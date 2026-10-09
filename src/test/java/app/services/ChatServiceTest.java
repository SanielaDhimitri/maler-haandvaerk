package app.services;

import app.api.GeminiApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void ask() {

        // Mock Gemini API
        GeminiApi geminiApi = mock(GeminiApi.class);

        // Simulerer svar fra Gemini
        when(geminiApi.askGemini("Hvad koster maling?"))
                .thenReturn("Prisen afhænger af opgaven.");

        ChatService chatService =
                new ChatService(geminiApi);

        // Kalder service
        String response =
                chatService.ask("Hvad koster maling?");

        // Kontrollerer svaret
        assertEquals(
                "Prisen afhænger af opgaven.",
                response
        );

        // Kontrollerer at Gemini API blev kaldt
        verify(geminiApi).askGemini(
                "Hvad koster maling?"
        );
    }
}

        //ChatServiceTest kontrollerer, at ChatService kommunikerer korrekt med GeminiApi