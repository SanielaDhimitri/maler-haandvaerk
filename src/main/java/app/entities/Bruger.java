package app.entities;

import app.enums.Rolle;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Bruger extends Person {

    // En bruger kan have mange bookinger
    @OneToMany(
            mappedBy = "bruger",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Booking> bookings = new ArrayList<>();


    // En bruger kan have mange projekter
    @OneToMany(
            mappedBy = "bruger",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Project> projects = new ArrayList<>();


    public Bruger(
            String navn,
            String email,
            String password,
            String telefon
    ) {
        super(navn, email, password, telefon, Rolle.BRUGER);
    }
}