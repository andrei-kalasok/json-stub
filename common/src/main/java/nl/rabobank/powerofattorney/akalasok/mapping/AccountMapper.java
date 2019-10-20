package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper extends AbstractMapper<Account, AccountDto> {

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;

    @Autowired
    public AccountMapper(UserRepository userRepository, AuthorizationRepository authorizationRepository) {
        this.userRepository = userRepository;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    protected Account newEntity() {
        return new Account();
    }

    @Override
    protected AccountDto newDto() {
        return new AccountDto();
    }

    @Override
    protected void customEntityMapping(AccountDto dto, Account entity) {
        User owner = userRepository.findById(dto.getOwner())
                .orElseThrow(() -> new ImpossibleMapping(String.format("Owner [%s] not found", dto.getOwner())));

        entity.setOwner(owner);
        authorizationRepository.save(Authority.authorization(owner, AssetType.ACCOUNT.authorityFor("OWNER", dto.getId())));
    }

    @Override
    protected void customDtoMapping(Account entity, AccountDto dto) {
        dto.setOwner(entity.getOwner().getUsername());
    }
}
