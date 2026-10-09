package app.entities;

import app.enums.ServiceType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ServiceType type;

    private String beskrivelse;


    // =========================
    // BOOKING
    // En service kan være tilknyttet flere bookinger
    // =========================

    @ManyToMany(mappedBy = "services")
    private List<Booking> bookings = new ArrayList<>();


    // =========================
    // WORK REQUEST DETAIL
    // En service kan bruges i flere WorkRequestDetails
    // =========================

    @OneToMany(mappedBy = "service")
    private List<WorkRequestDetail> workRequestDetails = new ArrayList<>();


    // =========================
    // PROJECT
    // En service kan være tilknyttet flere projekter
    // =========================

    @OneToMany(mappedBy = "service")
    private List<Project> projects = new ArrayList<>();


    // =========================
    // CONSTRUCTOR
    // =========================

    public Service(ServiceType type, String beskrivelse) {

        // BUSINESS RULE
        // ServiceType skal være valgt
        if (type == null) {
            throw new IllegalArgumentException(
                    "ServiceType skal vælges"
            );
        }

        // BUSINESS RULE
        // Beskrivelse må ikke være tom
        if (beskrivelse == null || beskrivelse.isBlank()) {
            throw new IllegalArgumentException(
                    "Beskrivelse må ikke være tom"
            );
        }

        this.type = type;
        this.beskrivelse = beskrivelse;
    }


    // =========================
    // SET TYPE
    // =========================

    public void setType(ServiceType type) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "ServiceType skal vælges"
            );
        }

        this.type = type;
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
}