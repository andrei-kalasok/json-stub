package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
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
public class DebitCardMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @InjectMocks
    private DebitCardMapper mapper;

    private DebitCardDto dto;
    private User cardHolder;

    @Before
    public void setUp() {
        cardHolder = new User();
        cardHolder.setUsername("cardHolder");
        when(userRepository.findByUsername("cardHolder")).thenReturn(Optional.of(cardHolder));

        dto = new DebitCardDto();
        dto.setId("002");
        DebitCardDto.Limit atmLimit = new DebitCardDto.Limit();
        atmLimit.setLimit(1.);
        atmLimit.setPeriodUnit("PER_WEEK");
        dto.setAtmLimit(atmLimit);
        DebitCardDto.Limit posLimit = new DebitCardDto.Limit();
        posLimit.setLimit(2.);
        posLimit.setPeriodUnit("PER_MONTH");
        dto.setPosLimit(posLimit);
        dto.setCardHolder("cardHolder");
    }

    @Test
    public void dtoToEntity() {
        DebitCard entity = mapper.toEntity(dto);

        assertEquals(dto.getAtmLimit().getLimit(), entity.getAtmLimitValue());
        assertEquals(dto.getAtmLimit().getPeriodUnit(), entity.getAtmLimitPeriodUnit());
        assertEquals(dto.getPosLimit().getLimit(), entity.getPosLimitValue());
        assertEquals(dto.getPosLimit().getPeriodUnit(), entity.getPosLimitPeriodUnit());
        assertEquals(cardHolder, entity.getCardHolder());
    }

    @Test
    public void storeAuthorities() {
        mapper.toEntity(dto);

        verify(authorizationRepository).save(eq(Authority.authorization(cardHolder, "DEBIT_CARD+HOLDER+" + dto.getId())));
    }

    @Test
    public void toEntityWithWrongUserName() {
        dto.setCardHolder("wrongUserName");
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("User [wrongUserName] not found", e.getMessage());
        }
    }

        @Test
        public void toDtoWithCustomFields() {
        DebitCard entity = new DebitCard();
        entity.setAtmLimitValue(12.);
        entity.setAtmLimitPeriodUnit("PER_WEEK");
        entity.setPosLimitValue(34.);
        entity.setPosLimitPeriodUnit("PER_DAY");
        entity.setCardHolder(cardHolder);

        DebitCardDto dto = mapper.toDto(entity);

        assertEquals(entity.getAtmLimitValue(), dto.getAtmLimit().getLimit());
        assertEquals(entity.getAtmLimitPeriodUnit(), dto.getAtmLimit().getPeriodUnit());
        assertEquals(entity.getPosLimitValue(), dto.getPosLimit().getLimit());
        assertEquals(entity.getPosLimitPeriodUnit(), dto.getPosLimit().getPeriodUnit());
        assertEquals(entity.getCardHolder().getUsername(), dto.getCardHolder());
    }
}