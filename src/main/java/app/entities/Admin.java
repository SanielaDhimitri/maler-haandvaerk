package app.entities;

import app.enums.Rolle;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Admin extends Person {

    public Admin(
            String navn,
            String email,
            String password,
            String telefon,
            Rolle rolle
    ) {
        super(navn, email, password, telefon, rolle);

        if (rolle != Rolle.ADMIN && rolle != Rolle.OWNER) {
            throw new IllegalArgumentException(
                    "Admin kan kun have rollen ADMIN eller OWNER"
            );
        }
    }
}

//her har vi permissions, ikke relations