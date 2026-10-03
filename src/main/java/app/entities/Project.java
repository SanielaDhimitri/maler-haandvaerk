package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String navn;

    @Column(nullable = false)
    private LocalDate startDato;

    @Column(nullable = false)
    private LocalDate slutDato;

    @Column(nullable = false)
    private BigDecimal pris;

    // Fremdrift fra 0 til 100
    private int fremdrift;


    // =========================
    // BRUGER
    // Mange projekter kan tilhøre én bruger
    // =========================

    @ManyToOne
    @JoinColumn(name = "bruger_id", nullable = false)
    private Bruger bruger;


    // =========================
    // SERVICE
    // Mange projekter kan have samme service
    // =========================

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;


    // =========================
    // MEDARBEJDERE
    // Et projekt kan have flere medarbejdere
    // En medarbejder kan arbejde på flere projekter
    // =========================

    @ManyToMany
    @JoinTable(
            name = "project_medarbejder",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "medarbejder_id")
    )
    private List<Medarbejder> medarbejdere = new ArrayList<>();


    public Project(
            String navn,
            LocalDate startDato,
            LocalDate slutDato,
            BigDecimal pris,
            Bruger bruger,
            Service service
    ) {
        this.navn = navn;
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.pris = pris;
        this.bruger = bruger;
        this.service = service;
        this.fremdrift = 0;
    }
}
