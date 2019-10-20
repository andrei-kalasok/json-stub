package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DebitCardMapper extends AbstractMapper<DebitCard, DebitCardDto> {

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;

    @Autowired
    public DebitCardMapper(UserRepository userRepository, AuthorizationRepository authorizationRepository) {
        this.userRepository = userRepository;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    protected DebitCard newEntity() {
        return new DebitCard();
    }

    @Override
    protected DebitCardDto newDto() {
        return new DebitCardDto();
    }

    @Override
    protected void customEntityMapping(DebitCardDto dto, DebitCard entity) {
        entity.setAtmLimitValue(dto.getAtmLimit().getLimit());
        entity.setAtmLimitPeriodUnit(dto.getAtmLimit().getPeriodUnit());
        entity.setPosLimitValue(dto.getPosLimit().getLimit());
        entity.setPosLimitPeriodUnit(dto.getPosLimit().getPeriodUnit());

        User cardHolder = userRepository.findByUsername(dto.getCardHolder())
                .orElseThrow(() -> new ImpossibleMapping(String.format("User [%s] not found", dto.getCardHolder())));
        entity.setCardHolder(cardHolder);

        authorizationRepository.save(Authority.authorization(cardHolder, AssetType.DEBIT_CARD.authorityFor("HOLDER", dto.getId())));
    }

    @Override
    protected void customDtoMapping(DebitCard entity, DebitCardDto dto) {
        dto.setAtmLimit(new DebitCardDto.Limit());
        dto.getAtmLimit().setLimit(entity.getAtmLimitValue());
        dto.getAtmLimit().setPeriodUnit(entity.getAtmLimitPeriodUnit());
        dto.setPosLimit(new DebitCardDto.Limit());
        dto.getPosLimit().setLimit(entity.getPosLimitValue());
        dto.getPosLimit().setPeriodUnit(entity.getPosLimitPeriodUnit());
        dto.setCardHolder(entity.getCardHolder().getUsername());
    }
}
