package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
import nl.rabobank.powerofattorney.akalasok.mapping.DebitCardMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.DebitCard;
import nl.rabobank.powerofattorney.akalasok.persistence.DebitCardRepository;
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
public class DebitCardServiceTest {

    @Mock
    private DebitCardRepository debitCardRepository;

    @Mock
    private DebitCardMapper debitCardMapper;

    @InjectMocks
    private DebitCardService debitCardService;

    @Before
    public void setUp() {
        when(debitCardMapper.toDto(any(DebitCard.class))).thenReturn(new DebitCardDto());
    }

    @Test
    public void entityNotFound() {
        assertTrue(debitCardService.getById("1").isEmpty());
    }

    @Test
    public void entityFound() {
        when(debitCardRepository.findById("1")).thenReturn(Optional.of(new DebitCard()));

        assertTrue(debitCardService.getById("1").isPresent());
    }

}