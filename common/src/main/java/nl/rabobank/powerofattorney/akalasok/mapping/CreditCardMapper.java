package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapper extends AbstractMapper<CreditCard, CreditCardDto> {

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;

    @Autowired
    public CreditCardMapper(UserRepository userRepository, AuthorizationRepository authorizationRepository) {
        this.userRepository = userRepository;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    protected CreditCard newEntity() {
        return new CreditCard();
    }

    @Override
    protected CreditCardDto newDto() {
        return new CreditCardDto();
    }

    @Override
    protected void customEntityMapping(CreditCardDto dto, CreditCard entity) {
        User cardHolder = userRepository.findByUsername(dto.getCardHolder())
                .orElseThrow(() -> new ImpossibleMapping(String.format("User [%s] not found", dto.getCardHolder())));
        entity.setCardHolder(cardHolder);

        authorizationRepository.save(Authority.authorization(cardHolder, AssetType.CREDIT_CARD.authorityFor("HOLDER", dto.getId())));
    }

    @Override
    protected void customDtoMapping(CreditCard entity, CreditCardDto dto) {
        dto.setCardHolder(entity.getCardHolder().getUsername());
    }
}
