package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @InjectMocks
    private AccountMapper mapper;

    private AccountDto dto;
    private User owner;

    @Before
    public void setUp() {
        dto = new AccountDto();
        dto.setId("123456");
        dto.setOwner("owner");

        owner = new User();
        owner.setUsername("owner");

        when(userRepository.findById("owner")).thenReturn(Optional.of(owner));
    }

    @Test
    public void mapUserToEntity() {
        Account account = mapper.toEntity(dto);

        assertSame(owner, account.getOwner());
    }

    @Test
    public void storeAuthorities() {
        mapper.toEntity(dto);

        verify(authorizationRepository).save(eq(Authority.authorization(owner, "ACCOUNT+OWNER+" + dto.getId())));
    }

    @Test
    public void mapWithWrongOwner() {
        dto.setOwner("wrongOwner");
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("Owner [wrongOwner] not found", e.getMessage());
        }
    }

    @Test
    public void toDtoWithCustomFields() {
        Account entity = new Account();
        entity.setOwner(owner);

        AccountDto dto = mapper.toDto(entity);

        assertEquals(entity.getOwner().getUsername(), dto.getOwner());
    }
}