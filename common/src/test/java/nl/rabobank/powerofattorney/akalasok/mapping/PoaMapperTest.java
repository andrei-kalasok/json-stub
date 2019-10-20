package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
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
public class PoaMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private DebitCardRepository debitCardRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @InjectMocks
    private PoaMapper mapper;

    private User grantor;
    private User grantee;

    private CreditCard creditCard;
    private DebitCard debitCard;
    private PoaDto dto;

    @Before
    public void setUp() {
        grantor = new User();
        grantor.setUsername("grantor");
        grantee = new User();
        grantee.setUsername("grantee");

        when(userRepository.findByUsername("grantor")).thenReturn(Optional.of(grantor));
        when(userRepository.findByUsername("grantee")).thenReturn(Optional.of(grantee));

        creditCard = new CreditCard();
        creditCard.setId("123");
        debitCard = new DebitCard();
        debitCard.setId("321");

        when(creditCardRepository.findById("123")).thenReturn(Optional.of(creditCard));
        when(debitCardRepository.findById("321")).thenReturn(Optional.of(debitCard));

        dto = new PoaDto();
        dto.setId("54321");
        dto.setGrantor("grantor");
        dto.setGrantee("grantee");
        dto.getCards().add(card("123", "CREDIT_CARD"));
        dto.getCards().add(card("321", "DEBIT_CARD"));
        dto.getAuthorizations().add("VIEW");
        dto.getAuthorizations().add("PAYMENT");
    }

    private PoaDto.Card card(String id, String type) {
        PoaDto.Card card = new PoaDto.Card();
        card.setId(id);
        card.setType(type);
        return card;
    }

    @Test
    public void toEntityMapping() {
        Poa entity = mapper.toEntity(dto);

        assertEquals(grantor, entity.getGrantor());
        assertEquals(grantee, entity.getGrantee());
        assertTrue(entity.getCards().contains(creditCard));
        assertTrue(entity.getCards().contains(debitCard));
        assertEquals("[VIEW, PAYMENT]", entity.getAuthorizations());
    }

    @Test
    public void mapWrongCreditCard() {
        dto.getCards().add(card("wrongId", "CREDIT_CARD"));
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("CreditCard with [wrongId] not found", e.getMessage());
        }
    }

    @Test
    public void mapWrongDebitCard() {
        dto.getCards().add(card("wrongId", "DEBIT_CARD"));
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("DebitCard with [wrongId] not found", e.getMessage());
        }
    }

    @Test
    public void wrongGrantorOwnerName() {
        dto.setGrantor("wrongGrantor");
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("Grantor [wrongGrantor] not found", e.getMessage());
        }
    }

    @Test
    public void wrongGranteeOwnerName() {
        dto.setGrantee("wrongGrantee");
        try {
            mapper.toEntity(dto);
            fail("Should fail");
        } catch (ImpossibleMapping e) {
            assertEquals("Grantee [wrongGrantee] not found", e.getMessage());
        }
    }

    @Test
    public void storeAuthoritiesForCreditCard() {
        mapper.toEntity(dto);

        verify(authorizationRepository).save(eq(Authority.authorization(grantee, "CREDIT_CARD+GRANTEE+" + creditCard.getId())));
        verify(authorizationRepository).save(eq(Authority.authorization(grantee, "DEBIT_CARD+GRANTEE+" + debitCard.getId())));
        verify(authorizationRepository).save(eq(Authority.authorization(grantor, "CREDIT_CARD+GRANTOR+" + creditCard.getId())));
        verify(authorizationRepository).save(eq(Authority.authorization(grantor, "DEBIT_CARD+GRANTOR+" + debitCard.getId())));
        verify(authorizationRepository).save(eq(Authority.authorization(grantee, "POA+GRANTEE+" + dto.getId())));
        verify(authorizationRepository).save(eq(Authority.authorization(grantor, "POA+GRANTOR+" + dto.getId())));
    }

    @Test
    public void toDtoWithCustomFields() {
        Poa entity = new Poa();
        entity.setGrantor(grantor);
        entity.setGrantee(grantee);
        CreditCard creditCard = new CreditCard();
        creditCard.setId("1");
        entity.getCards().add(creditCard);
        DebitCard debitCard = new DebitCard();
        debitCard.setId("2");
        entity.getCards().add(debitCard);
        entity.setAuthorizations("[VIEW, PAYMENT]");

        PoaDto dto = mapper.toDto(entity);

        assertEquals(entity.getGrantor().getUsername(), dto.getGrantor());
        assertEquals(entity.getGrantee().getUsername(), dto.getGrantee());
        assertEquals("1", dto.getCards().get(0).getId());
        assertEquals("CREDIT_CARD", dto.getCards().get(0).getType());
        assertEquals("2", dto.getCards().get(1).getId());
        assertEquals("DEBIT_CARD", dto.getCards().get(1).getType());
        assertEquals("VIEW", dto.getAuthorizations().get(0));
        assertEquals("PAYMENT", dto.getAuthorizations().get(1));
    }
}