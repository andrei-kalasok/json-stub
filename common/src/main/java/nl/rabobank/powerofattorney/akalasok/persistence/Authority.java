package nl.rabobank.powerofattorney.akalasok.persistence;

import lombok.*;

import javax.persistence.*;

@Entity(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
    private String authority;

    public static Authority authorization(User user, String authority) {
        Authority authorization = new Authority();
        authorization.user = user;
        authorization.authority = authority;
        return authorization;
    }
}
