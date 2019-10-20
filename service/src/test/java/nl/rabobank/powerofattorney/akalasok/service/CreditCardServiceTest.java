package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.mapping.CreditCardMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.CreditCard;
import nl.rabobank.powerofattorney.akalasok.persistence.CreditCardRepository;
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
public class CreditCardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private CreditCardMapper creditCardMapper;

    @InjectMocks
    private CreditCardService creditCardService;

    @Before
    public void setUp() {
        when(creditCardMapper.toDto(any(CreditCard.class))).thenReturn(new CreditCardDto());
    }

    @Test
    public void entityNotFound() {
        assertTrue(creditCardService.getById("1").isEmpty());
    }

    @Test
    public void entityFound() {
        when(creditCardRepository.findById("1")).thenReturn(Optional.of(new CreditCard()));

        assertTrue(creditCardService.getById("1").isPresent());
    }

}