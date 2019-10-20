package nl.rabobank.powerofattorney.akalasok.model;

import java.util.List;

/**
 * Application asset types:
 * supportedRoles - list of roles, which can be associated with the asset type
 */
public enum AssetType {
    ACCOUNT("OWNER"),
    CREDIT_CARD("HOLDER", "GRANTOR", "GRANTEE"),
    DEBIT_CARD("HOLDER", "GRANTOR", "GRANTEE"),
    POA("GRANTOR", "GRANTEE");

    private final List<String> supportedRoles;

    AssetType(String... supportedRoles) {
        this.supportedRoles = List.of(supportedRoles);
    }

    /**
     * Parse an authority string to extract an asset type, an user role and an asset id
     *
     * @param authority an authority string
     * @return an asset authority object, which contains an asset type, an user role and an asset id
     */
    public static AssetAuthority parseAuthority(String authority) {
        String[] authorityParts = authority.split("\\+");
        if (authorityParts.length != 3) {
            throw new IllegalArgumentException("Bad authority format, expected: type+role+id, but got: " + authority);
        }

        String assetType = authorityParts[0];
        String role = authorityParts[1];
        String assetId = authorityParts[2];

        for (AssetType type : values()) {
            if (type.name().equals(assetType)) {
                if (!type.supportedRoles.contains(role)) {
                    throw new IllegalArgumentException(String.format("Unsupported role [%s] for asset type [%s]", role, type.name()));
                }

                return new AssetAuthority(type, role, assetId);
            }
        }

        throw new IllegalArgumentException("Bad authority format, expected: type+role+id, but got: " + authority);
    }

    /**
     * List of supported roles for the asset type
     *
     * @return list of supported roles
     */
    public List<String> supportedRoles() {
        return supportedRoles;
    }

    /**
     * Build authority string for an user based on role and an asset id
     *
     * @param role role
     * @param id   an asset id
     * @return Authority string
     */
    public String authorityFor(String role, String id) {
        if (!supportedRoles.contains(role)) {
            throw new IllegalArgumentException(String.format("Unsupported role [%s] for asset type [%s]", role, name()));
        }

        return name() + "+" + role + "+" + id;
    }
}
