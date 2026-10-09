package app.entities;

import app.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dato;
    private LocalTime tid;

    private String beskrivelse;

    // Kontaktoplysninger - bruges også ved booking uden login
    private String kundenavn;
    private String email;
    private String telefon;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    // Kan være null, hvis kunden booker uden login
    @ManyToOne
    @JoinColumn(name = "bruger_id", nullable = true)
    private Bruger bruger;

    @ManyToMany
    @JoinTable(
            name = "booking_service",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services = new HashSet<>();

    public Booking(
            LocalDate dato,
            LocalTime tid,
            String beskrivelse,
            String kundenavn,
            String email,
            String telefon,
            Bruger bruger
    ) {

        // BUSINESS RULE
        // En booking må ikke oprettes med en dato i fortiden
        if (dato != null && dato.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Bookingdato må ikke være i fortiden"
            );
        }

        this.dato = dato;
        this.tid = tid;
        this.beskrivelse = beskrivelse;
        this.kundenavn = kundenavn;
        this.email = email;
        this.telefon = telefon;
        this.bruger = bruger;

        // Nye bookinger starter altid med status AFVENTER
        this.status = BookingStatus.AFVENTER;
    }


    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setDato(LocalDate dato) {

        // BUSINESS RULE
        // Bookingdato må ikke være i fortiden
        if (dato != null && dato.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Bookingdato må ikke være i fortiden"
            );
        }

        this.dato = dato;
    }

    public void setTid(LocalTime tid) {
        this.tid = tid;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void addService(Service service) {
        this.services.add(service);
    }
    public void setKundenavn(String kundenavn) {
        this.kundenavn = kundenavn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setServices(Set<Service> services) {
        this.services.clear();
        this.services.addAll(services);
    }
}