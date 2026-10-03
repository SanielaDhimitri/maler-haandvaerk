package app.entities;

import app.enums.Rolle;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Medarbejder extends Person {

    @ManyToMany(mappedBy = "medarbejdere")
    private List<Project> projects = new ArrayList<>();

    public Medarbejder(
            String navn,
            String email,
            String password,
            String telefon
    ) {
        super(navn, email, password, telefon, Rolle.MEDARBEJDER);
    }
}