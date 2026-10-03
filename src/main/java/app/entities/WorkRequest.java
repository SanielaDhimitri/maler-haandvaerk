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

    @OneToMany(
            mappedBy = "workRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WorkRequestDetail> workRequestDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "bruger_id", nullable = true)
    private Bruger bruger;

    @OneToOne(
            mappedBy = "workRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Offer offer;

    public void addService(Service service,
                           String beskrivelse,
                           Double omfang,
                           String enhed) {

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

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public void setEfternavn(String efternavn) {
        this.efternavn = efternavn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public WorkRequest(String fornavn,
                       String efternavn,
                       String email,
                       String telefon,
                       String adresse,
                       String beskrivelse,
                       Bruger bruger) {

        this.fornavn = fornavn;
        this.efternavn = efternavn;
        this.email = email;
        this.telefon = telefon;
        this.adresse = adresse;
        this.beskrivelse = beskrivelse;
        this.bruger = bruger;
        this.status = RequestStatus.NY;
    }
}