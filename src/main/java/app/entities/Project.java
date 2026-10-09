package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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


    // =========================
    // CONSTRUCTOR
    // =========================

    public Project(
            String navn,
            LocalDate startDato,
            LocalDate slutDato,
            BigDecimal pris,
            Bruger bruger,
            Service service
    ) {

        // BUSINESS RULE
        // Slutdato må ikke være før startdato
        if (startDato != null
                && slutDato != null
                && slutDato.isBefore(startDato)) {

            throw new IllegalArgumentException(
                    "Slutdato må ikke være før startdato"
            );
        }

        // BUSINESS RULE
        // Prisen skal være større end 0
        if (pris != null && pris.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Pris skal være større end 0"
            );
        }

        this.navn = navn;
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.pris = pris;
        this.bruger = bruger;
        this.service = service;

        // Et nyt projekt starter med 0 % fremdrift
        this.fremdrift = 0;
    }


    // =========================
    // SET NAVN
    // =========================

    public void setNavn(String navn) {
        this.navn = navn;
    }


    // =========================
    // SET START DATO
    // =========================

    public void setStartDato(LocalDate startDato) {

        // BUSINESS RULE
        // Slutdato må ikke være før startdato
        if (startDato != null
                && slutDato != null
                && slutDato.isBefore(startDato)) {

            throw new IllegalArgumentException(
                    "Slutdato må ikke være før startdato"
            );
        }

        this.startDato = startDato;
    }


    // =========================
    // SET SLUT DATO
    // =========================

    public void setSlutDato(LocalDate slutDato) {

        // BUSINESS RULE
        // Slutdato må ikke være før startdato
        if (startDato != null
                && slutDato != null
                && slutDato.isBefore(startDato)) {

            throw new IllegalArgumentException(
                    "Slutdato må ikke være før startdato"
            );
        }

        this.slutDato = slutDato;
    }


    // =========================
    // SET PRIS
    // =========================

    public void setPris(BigDecimal pris) {

        // BUSINESS RULE
        // Prisen skal være større end 0
        if (pris != null && pris.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Pris skal være større end 0"
            );
        }

        this.pris = pris;
    }


    // =========================
    // SET FREMDRIFT
    // =========================

    public void setFremdrift(int fremdrift) {

        // BUSINESS RULE
        // Fremdrift skal være mellem 0 og 100
        if (fremdrift < 0 || fremdrift > 100) {
            throw new IllegalArgumentException(
                    "Fremdrift skal være mellem 0 og 100"
            );
        }

        this.fremdrift = fremdrift;
    }


    // =========================
    // SET BRUGER
    // =========================

    public void setBruger(Bruger bruger) {
        this.bruger = bruger;
    }


    // =========================
    // SET SERVICE
    // =========================

    public void setService(Service service) {
        this.service = service;
    }


    // =========================
    // ADD MEDARBEJDER
    // =========================

    public void addMedarbejder(Medarbejder medarbejder) {

        if (medarbejder != null
                && !medarbejdere.contains(medarbejder)) {

            medarbejdere.add(medarbejder);
        }
    }


    // =========================
    // REMOVE MEDARBEJDER
    // =========================

    public void removeMedarbejder(Medarbejder medarbejder) {
        medarbejdere.remove(medarbejder);
    }
}