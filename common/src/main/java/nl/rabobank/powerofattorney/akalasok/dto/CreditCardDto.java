package nl.rabobank.powerofattorney.akalasok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class CreditCardDto {

    @Id
    private String id;
    private Integer cardNumber;
    private String status;
    private Integer sequenceNumber;
    private String cardHolder;
    private Double monthlyLimit;
}
