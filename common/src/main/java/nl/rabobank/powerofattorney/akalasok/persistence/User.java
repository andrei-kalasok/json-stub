package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    @Id
    private String username;
    private String password;
}
