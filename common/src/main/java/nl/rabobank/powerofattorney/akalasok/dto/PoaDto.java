package nl.rabobank.powerofattorney.akalasok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PoaDto {

    private String id;
    private String grantor;
    private String grantee;
    private String account;
    private String direction;
    private List<Card> cards = new ArrayList<>();
    private List<String> authorizations = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Card {
        private String id;
        private String type;
    }
}
