package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cardType",
        discriminatorType = DiscriminatorType.STRING)
public class Card {

    @Id
    private String id;
    private Integer cardNumber;
    private String status;
    private Integer sequenceNumber;
    @ManyToOne
    @JoinColumn(name = "cardHolder")
    private User cardHolder;
}
