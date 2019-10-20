package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.mapping.AccountMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.Account;
import nl.rabobank.powerofattorney.akalasok.persistence.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Before
    public void setUp() {
        when(accountMapper.toDto(any(Account.class))).thenReturn(new AccountDto());
    }

    @Test
    public void entityNotFound() {
        assertTrue(accountService.getById("1").isEmpty());
    }

    @Test
    public void entityFound() {
        when(accountRepository.findById("1")).thenReturn(Optional.of(new Account()));

        assertTrue(accountService.getById("1").isPresent());
    }
}