package app.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashSet;
import java.util.Set;
import app.security.ISecurityUser;

import java.nio.charset.StandardCharsets;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements ISecurityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String navn;
    private String email;
    @JsonIgnore
    @Column(name = "password_hash", length = 60, nullable = false)
    private String passwordHash;
    private String telefon;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    private Set<Role> roles = new HashSet<>();

    // =========================
    // CONSTRUCTOR
    // =========================

    public Person(
            String navn,
            String email,
            String password,
            String telefon

    ) {
        this.navn = navn;
        this.email = email;
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password cannot be null or blank"
            );
        }

        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password cannot be longer than 72 UTF-8 bytes"
            );
        }

        this.passwordHash =
                BCrypt.hashpw(password, BCrypt.gensalt(12));
        //Password → validim → bcrypt → hash → database


        this.telefon = telefon;

    }


    // =========================
    // SET NAVN
    // =========================

    public void setNavn(String navn) {
        this.navn = navn;
    }


    // =========================
    // SET EMAIL
    // =========================

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================
    // SET TELEFON
    // =========================

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    @Override
    public boolean verifyPassword(String password) {

        if (password == null) {
            return false;
        }

        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            return false;
        }

        return BCrypt.checkpw(password, passwordHash);
    }
    @Override
    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public void removeRole(String role) {
        roles.removeIf(r -> r.getRoleName().equalsIgnoreCase(role));
    }

    @Override
    public Set<String> getRolesAsStrings() {
        Set<String> roleNames = new HashSet<>();

        for (Role role : roles) {
            roleNames.add(role.getRoleName());
        }

        return roleNames;
    }
}