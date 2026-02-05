package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Table(name = "teachers")
public class Teacher extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID  uuid;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String vat;

    @PrePersist
    public void initializeUUID(){
        this.uuid = UUID.randomUUID();
    }

    //TODO List<T> region = new ArrayList<>();
}
