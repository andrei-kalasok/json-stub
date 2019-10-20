package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Poa {

    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "grantor")
    private User grantor;
    @ManyToOne
    @JoinColumn(name = "grantee")
    private User grantee;
    private String account;
    private String direction;
    @ManyToMany
    @JoinTable(
            name = "Poa_Card",
            joinColumns = @JoinColumn(name = "poa_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> cards = new ArrayList<>();
    private String authorizations;
}
