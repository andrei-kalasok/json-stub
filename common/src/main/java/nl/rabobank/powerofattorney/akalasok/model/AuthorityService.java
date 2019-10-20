package nl.rabobank.powerofattorney.akalasok.model;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.rabobank.powerofattorney.akalasok.model.AssetType.*;

@Service
public class AuthorityService {

    /**
     * Build assets overview based on an authorities list
     *
     * @param authorities list of authorities
     * @return AuthorityOverview overview object, which contains all accounts, credit cards, debit cards and power-of-authorities
     * associated with an user  list of authorities
     */
    public AuthorityOverview buildOverview(List<String> authorities) {
        List<AssetAuthority> accounts = assetAuthorityByType(ACCOUNT, authorities).collect(Collectors.toList());
        List<AssetAuthority> creditCards = assetAuthorityByType(CREDIT_CARD, authorities).collect(Collectors.toList());
        List<AssetAuthority> debitCards = assetAuthorityByType(DEBIT_CARD, authorities).collect(Collectors.toList());
        List<AssetAuthority> poas = assetAuthorityByType(POA, authorities).collect(Collectors.toList());

        return new AuthorityOverview(accounts, creditCards, debitCards, poas);
    }

    private Stream<AssetAuthority> assetAuthorityByType(AssetType type, List<String> authorities) {
        return authorities.stream()
                .map(AssetType::parseAuthority)
                .filter(authority -> type == authority.getType());
    }

    /**
     * Is access to the asset is allowed based on an authorities list
     *
     * @param assetType   asset type
     * @param assetId     asset id
     * @param authorities authorities list
     * @return true if access is allowed, false otherwise
     */
    public boolean isAccessAllowed(AssetType assetType, String assetId, List<String> authorities) {
        for (String role : assetType.supportedRoles()) {
            if (authorities.contains(assetType.authorityFor(role, assetId))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get accessible assets' ids by type based on an authorities list
     *
     * @param type        asset type
     * @param authorities authorities list
     * @return list of accessible assets' ids
     */
    public List<String> getAccessibleAssetIdsByType(AssetType type, List<String> authorities) {
        return assetAuthorityByType(type, authorities)
                .map(AssetAuthority::getId)
                .collect(Collectors.toList());
    }
}
