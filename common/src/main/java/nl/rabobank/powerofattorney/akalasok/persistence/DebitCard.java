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
@DiscriminatorValue("DebitCard")
public class DebitCard extends Card {

    private Double atmLimitValue;
    private String atmLimitPeriodUnit;
    private Double posLimitValue;
    private String posLimitPeriodUnit;
    private Boolean contactless;
}
