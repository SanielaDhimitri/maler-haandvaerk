package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkRequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // WORK REQUEST
    // Mange detaljer kan tilhøre samme WorkRequest
    // =========================

    @ManyToOne
    @JoinColumn(name = "work_request_id", nullable = false)
    @ToString.Exclude
    private WorkRequest workRequest;


    // =========================
    // SERVICE
    // Mange detaljer kan bruge samme Service
    // =========================

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @ToString.Exclude
    private Service service;


    private String beskrivelse;
    private Double omfang;
    private String enhed;


    // =========================
    // CONSTRUCTOR
    // =========================

    public WorkRequestDetail(
            WorkRequest workRequest,
            Service service,
            String beskrivelse,
            Double omfang,
            String enhed
    ) {

        // BUSINESS RULE
        // WorkRequest skal angives
        if (workRequest == null) {
            throw new IllegalArgumentException(
                    "WorkRequest skal angives"
            );
        }

        // BUSINESS RULE
        // Service skal angives
        if (service == null) {
            throw new IllegalArgumentException(
                    "Service skal angives"
            );
        }

        // BUSINESS RULE
        // Beskrivelse må ikke være tom
        if (beskrivelse == null || beskrivelse.isBlank()) {
            throw new IllegalArgumentException(
                    "Beskrivelse må ikke være tom"
            );
        }

        // BUSINESS RULE
        // Omfang skal være større end 0
        if (omfang == null || omfang <= 0) {
            throw new IllegalArgumentException(
                    "Omfang skal være større end 0"
            );
        }

        // BUSINESS RULE
        // Enhed må ikke være tom
        if (enhed == null || enhed.isBlank()) {
            throw new IllegalArgumentException(
                    "Enhed må ikke være tom"
            );
        }

        this.workRequest = workRequest;
        this.service = service;
        this.beskrivelse = beskrivelse;
        this.omfang = omfang;
        this.enhed = enhed;
    }
}