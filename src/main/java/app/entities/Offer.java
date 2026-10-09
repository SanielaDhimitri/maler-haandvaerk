package app.entities;

import app.enums.OfferStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pris;

    private String beskrivelse;

    private LocalDate oprettetDato;

    private LocalDate gyldigTil;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;


    // =========================
    // WORK REQUEST
    // Ét Offer tilhører én WorkRequest
    // =========================

    @JsonIgnore
    @OneToOne
    @JoinColumn(
            name = "work_request_id",
            nullable = false,
            unique = true
    )
    private WorkRequest workRequest;


    // =========================
    // CONSTRUCTOR
    // =========================

    public Offer(
            Double pris,
            String beskrivelse,
            LocalDate oprettetDato,
            LocalDate gyldigTil,
            WorkRequest workRequest
    ) {

        // BUSINESS RULE
        // Prisen skal være større end 0
        if (pris != null && pris <= 0) {
            throw new IllegalArgumentException(
                    "Pris skal være større end 0"
            );
        }

        // BUSINESS RULE
        // GyldigTil må ikke være før oprettelsesdatoen
        if (oprettetDato != null
                && gyldigTil != null
                && gyldigTil.isBefore(oprettetDato)) {

            throw new IllegalArgumentException(
                    "GyldigTil må ikke være før oprettetDato"
            );
        }

        this.pris = pris;
        this.beskrivelse = beskrivelse;
        this.oprettetDato = oprettetDato;
        this.gyldigTil = gyldigTil;
        this.workRequest = workRequest;

        // Nye tilbud starter altid som AFVENTER
        this.status = OfferStatus.AFVENTER;
    }


    // =========================
    // SET STATUS
    // =========================

    public void setStatus(OfferStatus status) {
        this.status = status;
    }


    // =========================
    // SET PRIS
    // =========================

    public void setPris(Double pris) {

        // BUSINESS RULE
        // Prisen skal være større end 0
        if (pris != null && pris <= 0) {
            throw new IllegalArgumentException(
                    "Pris skal være større end 0"
            );
        }

        this.pris = pris;
    }


    // =========================
    // SET BESKRIVELSE
    // =========================

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }


    // =========================
    // SET OPRETTET DATO
    // =========================

    public void setOprettetDato(LocalDate oprettetDato) {

        // BUSINESS RULE
        // GyldigTil må ikke være før oprettelsesdatoen
        if (oprettetDato != null
                && gyldigTil != null
                && gyldigTil.isBefore(oprettetDato)) {

            throw new IllegalArgumentException(
                    "GyldigTil må ikke være før oprettetDato"
            );
        }

        this.oprettetDato = oprettetDato;
    }


    // =========================
    // SET GYLDIG TIL
    // =========================

    public void setGyldigTil(LocalDate gyldigTil) {

        // BUSINESS RULE
        // GyldigTil må ikke være før oprettelsesdatoen
        if (oprettetDato != null
                && gyldigTil != null
                && gyldigTil.isBefore(oprettetDato)) {

            throw new IllegalArgumentException(
                    "GyldigTil må ikke være før oprettetDato"
            );
        }

        this.gyldigTil = gyldigTil;
    }


    // =========================
    // SET WORK REQUEST
    // =========================

    public void setWorkRequest(WorkRequest workRequest) {
        this.workRequest = workRequest;
    }
}