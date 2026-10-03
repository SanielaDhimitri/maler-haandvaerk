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

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "work_request_id", nullable = false, unique = true)
    private WorkRequest workRequest;

    public Offer(Double pris,
                 String beskrivelse,
                 LocalDate oprettetDato,
                 LocalDate gyldigTil,
                 WorkRequest workRequest) {

        this.pris = pris;
        this.beskrivelse = beskrivelse;
        this.oprettetDato = oprettetDato;
        this.gyldigTil = gyldigTil;
        this.workRequest = workRequest;
        this.status = OfferStatus.AFVENTER;
    }

    public void setStatus(OfferStatus status)
    {
        this.status = status;
    }
    public void setPris(Double pris) {
        this.pris = pris;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setOprettetDato(LocalDate oprettetDato) {
        this.oprettetDato = oprettetDato;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public void setWorkRequest(WorkRequest workRequest) {
        this.workRequest = workRequest;
    }
}