package nl.rabobank.powerofattorney.akalasok.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuthorityOverview {

    private final List<AssetAuthority> accounts;
    private final List<AssetAuthority> creditCards;
    private final List<AssetAuthority> debitCards;
    private final List<AssetAuthority> poas;
}
