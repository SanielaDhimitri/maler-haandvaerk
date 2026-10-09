package app.security;

import app.entities.Person;
import app.entities.Role;

public interface ISecurityDAO {

    Person getVerifiedUser(String email, String password);

    Person createUser(
            String navn,
            String email,
            String password,
            String telefon
    );

    Role createRole(String role);

    Person addUserRole(String email, String role);
}

//ISecurityDAO definerer de metoder, der bruges til login, registrering og håndtering af brugerroller