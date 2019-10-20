package nl.rabobank.powerofattorney.akalasok.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class AssetAuthority {

    private final AssetType type;
    private final String role;
    private final String id;
}
