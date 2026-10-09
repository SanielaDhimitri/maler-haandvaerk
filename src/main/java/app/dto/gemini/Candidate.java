package app.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Candidate(Content content) {
}

//De fire records i gemini map, repræsenterer strukturen i JSON-svaret, som vi modtager fra Gemini API.