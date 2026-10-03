package app.entities;

import app.enums.Rolle;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String navn;
    private String email;
    private String password;
    private String telefon;

    @Enumerated(EnumType.STRING)
    private Rolle rolle;

    public Person(String navn, String email, String password,
                  String telefon, Rolle rolle) {
        this.navn = navn;
        this.email = email;
        this.password = password;
        this.telefon = telefon;
        this.rolle = rolle;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}