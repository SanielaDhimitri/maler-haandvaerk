package app.entities;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity//JPA entities
@NoArgsConstructor(access = AccessLevel.PROTECTED)//Protect JPA entities+costruct uden parametres for jpa
public class Admin extends Person {

    public Admin(
            String navn,
            String email,
            String password,
            String telefon

    ) {
        super(navn, email, password, telefon);


    }
}

//her har vi permissions, ikke relations