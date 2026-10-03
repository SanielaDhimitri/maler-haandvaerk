package app.entities;

import jakarta.persistence.*;
import lombok.*;
import app.enums.ServiceType;

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


    // Service <-> Booking
    @ManyToMany(mappedBy = "services")
    private List<Booking> bookings = new ArrayList<>();


    // Service <-> WorkRequestDetail
    @OneToMany(mappedBy = "service")
    private List<WorkRequestDetail> workRequestDetails = new ArrayList<>();


    // Service <-> Project
    @OneToMany(mappedBy = "service")
    private List<Project> projects = new ArrayList<>();


    public Service(ServiceType type, String beskrivelse) {
        this.type = type;
        this.beskrivelse = beskrivelse;
    }
}