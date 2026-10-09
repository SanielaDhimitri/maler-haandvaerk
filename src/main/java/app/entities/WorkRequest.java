package app.entities;

import app.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fornavn;
    private String efternavn;
    private String email;
    private String telefon;
    private String adresse;
    private String beskrivelse;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    // =========================
    // WORK REQUEST DETAILS
    // En WorkRequest kan have flere services/detaljer
    // =========================

    @OneToMany(
            mappedBy = "workRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WorkRequestDetail> workRequestDetails = new ArrayList<>();


    // =========================
    // BRUGER
    // Kan være null, hvis kunden ikke er logget ind
    // =========================

    @ManyToOne
    @JoinColumn(name = "bruger_id", nullable = true)
    private Bruger bruger;


    // =========================
    // OFFER
    // En WorkRequest kan have ét tilbud
    // =========================

    @OneToOne(
            mappedBy = "workRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Offer offer;


    // =========================
    // CONSTRUCTOR
    // =========================

    public WorkRequest(
            String fornavn,
            String efternavn,
            String email,
            String telefon,
            String adresse,
            String beskrivelse,
            Bruger bruger
    ) {

        // BUSINESS RULE
        // Fornavn må ikke være tomt
        if (fornavn == null || fornavn.isBlank()) {
            throw new IllegalArgumentException(
                    "Fornavn må ikke være tomt"
            );
        }

        // BUSINESS RULE
        // Efternavn må ikke være tomt
        if (efternavn == null || efternavn.isBlank()) {
            throw new IllegalArgumentException(
                    "Efternavn må ikke være tomt"
            );
        }

        // BUSINESS RULE
        // Email skal være angivet og have et gyldigt format
        if (email == null
                || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {

            throw new IllegalArgumentException(
                    "Email skal være gyldig"
            );
        }

        // BUSINESS RULE
        // Telefonnummer skal bestå af 8 cifre
        if (telefon == null || !telefon.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "Telefonnummer skal bestå af 8 cifre"
            );
        }

        // BUSINESS RULE
        // Adresse må ikke være tom
        if (adresse == null || adresse.isBlank()) {
            throw new IllegalArgumentException(
                    "Adresse må ikke være tom"
            );
        }

        // BUSINESS RULE
        // Beskrivelse må ikke være tom
        if (beskrivelse == null || beskrivelse.isBlank()) {
            throw new IllegalArgumentException(
                    "Beskrivelse må ikke være tom"
            );
        }

        this.fornavn = fornavn;
        this.efternavn = efternavn;
        this.email = email;
        this.telefon = telefon;
        this.adresse = adresse;
        this.beskrivelse = beskrivelse;
        this.bruger = bruger;

        // Nye WorkRequests starter altid med status NY
        this.status = RequestStatus.NY;
    }


    // =========================
    // ADD SERVICE
    // =========================

    public void addService(
            Service service,
            String beskrivelse,
            Double omfang,
            String enhed
    ) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service skal angives"
            );
        }

        if (beskrivelse == null || beskrivelse.isBlank()) {
            throw new IllegalArgumentException(
                    "Beskrivelse må ikke være tom"
            );
        }

        if (omfang == null || omfang <= 0) {
            throw new IllegalArgumentException(
                    "Omfang skal være større end 0"
            );
        }

        if (enhed == null || enhed.isBlank()) {
            throw new IllegalArgumentException(
                    "Enhed må ikke være tom"
            );
        }

        WorkRequestDetail workRequestDetail =
                new WorkRequestDetail(
                        this,
                        service,
                        beskrivelse,
                        omfang,
                        enhed
                );

        workRequestDetails.add(workRequestDetail);
    }


    // =========================
    // SET STATUS
    // =========================

    public void setStatus(RequestStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status må ikke være null"
            );
        }

        this.status = status;
    }


    // =========================
    // SET FORNAVN
    // =========================

    public void setFornavn(String fornavn) {

        if (fornavn == null || fornavn.isBlank()) {
            throw new IllegalArgumentException(
                    "Fornavn må ikke være tomt"
            );
        }

        this.fornavn = fornavn;
    }


    // =========================
    // SET EFTERNAVN
    // =========================

    public void setEfternavn(String efternavn) {

        if (efternavn == null || efternavn.isBlank()) {
            throw new IllegalArgumentException(
                    "Efternavn må ikke være tomt"
            );
        }

        this.efternavn = efternavn;
    }


    // =========================
    // SET EMAIL
    // =========================

    public void setEmail(String email) {

        if (email == null
                || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {

            throw new IllegalArgumentException(
                    "Email skal være gyldig"
            );
        }

        this.email = email;
    }


    // =========================
    // SET TELEFON
    // =========================

    public void setTelefon(String telefon) {

        if (telefon == null || !telefon.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "Telefonnummer skal bestå af 8 cifre"
            );
        }

        this.telefon = telefon;
    }


    // =========================
    // SET ADRESSE
    // =========================

    public void setAdresse(String adresse) {

        if (adresse == null || adresse.isBlank()) {
            throw new IllegalArgumentException(
                    "Adresse må ikke være tom"
            );
        }

        this.adresse = adresse;
    }


    // =========================
    // SET BESKRIVELSE
    // =========================

    public void setBeskrivelse(String beskrivelse) {

        if (beskrivelse == null || beskrivelse.isBlank()) {
            throw new IllegalArgumentException(
                    "Beskrivelse må ikke være tom"
            );
        }

        this.beskrivelse = beskrivelse;
    }


    // =========================
    // SET OFFER
    // =========================

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}