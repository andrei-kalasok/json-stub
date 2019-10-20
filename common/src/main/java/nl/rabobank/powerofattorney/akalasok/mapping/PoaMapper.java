package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PoaMapper extends AbstractMapper<Poa, PoaDto> {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final DebitCardRepository debitCardRepository;
    private final AuthorizationRepository authorizationRepository;

    @Autowired
    public PoaMapper(UserRepository userRepository, CreditCardRepository creditCardRepository, DebitCardRepository debitCardRepository, AuthorizationRepository authorizationRepository) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
        this.debitCardRepository = debitCardRepository;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    protected Poa newEntity() {
        return new Poa();
    }

    @Override
    protected PoaDto newDto() {
        return new PoaDto();
    }

    @Override
    protected void customEntityMapping(PoaDto dto, Poa entity) {
        User grantor = userRepository.findByUsername(dto.getGrantor())
                .orElseThrow(() -> new ImpossibleMapping(String.format("Grantor [%s] not found", dto.getGrantor())));
        entity.setGrantor(grantor);

        User grantee = userRepository.findByUsername(dto.getGrantee())
                .orElseThrow(() -> new ImpossibleMapping(String.format("Grantee [%s] not found", dto.getGrantee())));
        entity.setGrantee(grantee);

        entity.setCards(new ArrayList<>());
        dto.getCards().forEach(card -> {
            switch (card.getType()) {
                case "CREDIT_CARD":
                    CreditCard creditCard = creditCardRepository.findById(card.getId())
                            .orElseThrow(() -> new ImpossibleMapping(String.format("CreditCard with [%s] not found", card.getId())));
                    entity.getCards().add(creditCard);
                    addAuthorities(card, AssetType.CREDIT_CARD, grantor, grantee);

                    break;
                case "DEBIT_CARD":
                    DebitCard debitCard = debitCardRepository.findById(card.getId())
                            .orElseThrow(() -> new ImpossibleMapping(String.format("DebitCard with [%s] not found", card.getId())));
                    entity.getCards().add(debitCard);
                    addAuthorities(card, AssetType.DEBIT_CARD, grantor, grantee);
                    break;
            }
        });

        authorizationRepository.save(Authority.authorization(grantor, AssetType.POA.authorityFor("GRANTOR", dto.getId())));
        authorizationRepository.save(Authority.authorization(grantee, AssetType.POA.authorityFor("GRANTEE", dto.getId())));
    }

    private void addAuthorities(PoaDto.Card card, AssetType type, User grantor, User grantee) {
        authorizationRepository.save(Authority.authorization(grantor, type.authorityFor("GRANTOR", card.getId())));
        authorizationRepository.save(Authority.authorization(grantee, type.authorityFor("GRANTEE", card.getId())));
    }

    @Override
    protected void customDtoMapping(Poa entity, PoaDto dto) {
        dto.setGrantor(entity.getGrantor().getUsername());
        dto.setGrantee(entity.getGrantee().getUsername());

        for (Card card : entity.getCards()) {
            PoaDto.Card cardDto = new PoaDto.Card();
            cardDto.setId(card.getId());
            if (card instanceof CreditCard) {
                cardDto.setType("CREDIT_CARD");
            }
            if (card instanceof DebitCard) {
                cardDto.setType("DEBIT_CARD");
            }
            dto.getCards().add(cardDto);
        }

        String[] authorizations = entity.getAuthorizations().replace("[", "").replace("]", "").split(", ");
        for (String authorization : authorizations) {
            dto.getAuthorizations().add(authorization);
        }

    }
}
