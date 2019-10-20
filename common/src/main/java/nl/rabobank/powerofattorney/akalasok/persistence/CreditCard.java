package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CreditCard")
public class CreditCard extends Card {

    private Double monthlyLimit;
}
