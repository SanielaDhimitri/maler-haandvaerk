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

    @ManyToOne
    @JoinColumn(name = "work_request_id", nullable = false)
    @ToString.Exclude
    private WorkRequest workRequest;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @ToString.Exclude
    private Service service;

    private String beskrivelse;
    private Double omfang;
    private String enhed;

    public WorkRequestDetail(
            WorkRequest workRequest,
            Service service,
            String beskrivelse,
            Double omfang,
            String enhed) {

        this.workRequest = workRequest;
        this.service = service;
        this.beskrivelse = beskrivelse;
        this.omfang = omfang;
        this.enhed = enhed;
    }


}