package app.utils;

public class InputValidator {

    // ikke null
    public static boolean isNotEmpty(String text) {
        return text != null && !text.isBlank();
    }

    // Fornavn dhe efternavn:
    // ikke null+ stor bogstave
    public static boolean isValidName(String name) {
        return isNotEmpty(name)
                && Character.isUpperCase(name.charAt(0));
    }

    // Email:
    // ikke null+@
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email)
                && email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }

    // Telefon:
    // ikke null+ nr
    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone)
                && phone.matches("\\d{8}");
    }

    // Omfang:
    // større end 0
    public static boolean isValidOmfang(double omfang) {
        return omfang > 0;
    }
}