package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    private Double balance;
    private LocalDate created;
    private LocalDate ended;
}
