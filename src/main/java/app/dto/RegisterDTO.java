package app.dto;

public record RegisterDTO(
        String navn,
        String email,
        String password,
        String telefon
) {
}