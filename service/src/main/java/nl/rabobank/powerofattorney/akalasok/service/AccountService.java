package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.mapping.AccountMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.Account;
import nl.rabobank.powerofattorney.akalasok.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends AbstractService<Account, AccountDto> {

    @Autowired
    public AccountService(AccountRepository repository, AccountMapper mapper) {
        super(repository, mapper);
    }
}
