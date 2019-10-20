package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @InjectMocks
    private CreditCardMapper mapper;

    private CreditCardDto dto;
    private User cardHolder;

    @Before
    public void setUp() {
        cardHolder = new User();
        cardHolder.setUsername("cardHolder");

        when(userRepository.findByUsername("cardHolder")).thenReturn(Optional.of(cardHolder));

        dto = new CreditCardDto();
        dto.setId("001");
        dto.setCardHolder("cardHolder");
    }

    @Test
    public void dtoToEntity() {
        CreditCard entity = mapper.toEntity(dto);

        assertEquals(cardHolder, entity.getCardHolder());
    }

    @Test
    public void storeAuthorities() {
        mapper.toEntity(dto);

        verify(authorizationRepository).save(eq(Authority.authorization(cardHolder, "CREDIT_CARD+HOLDER+" + dto.getId())));
    }

    @Test
    public void toEntityWithWrongUserName() {
        dto.setCardHolder("wrongCardHolder");
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("User [wrongCardHolder] not found", e.getMessage());
        }
    }

    @Test
    public void toDtoWithCustomFields() {
        CreditCard entity = new CreditCard();
        entity.setCardHolder(cardHolder);

        CreditCardDto dto = mapper.toDto(entity);

        assertEquals(entity.getCardHolder().getUsername(), dto.getCardHolder());
    }
}