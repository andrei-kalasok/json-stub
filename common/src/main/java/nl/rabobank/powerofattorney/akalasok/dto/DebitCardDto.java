package nl.rabobank.powerofattorney.akalasok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class DebitCardDto {

    @Id
    private String id;
    private Integer cardNumber;
    private String status;
    private Integer sequenceNumber;
    private String cardHolder;
    private Limit atmLimit;
    private Limit posLimit;
    private Boolean contactless;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Limit {
        private Double limit;
        private String periodUnit;
    }
}
