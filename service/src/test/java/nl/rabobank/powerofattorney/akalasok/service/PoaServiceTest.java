package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.mapping.PoaMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.Poa;
import nl.rabobank.powerofattorney.akalasok.persistence.PoaRepository;
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
public class PoaServiceTest {

    @Mock
    private PoaRepository poaRepository;

    @Mock
    private PoaMapper poaMapper;

    @InjectMocks
    private PoaService poaService;

    @Before
    public void setUp() {
        when(poaMapper.toDto(any(Poa.class))).thenReturn(new PoaDto());
    }

    @Test
    public void entityNotFound() {
        assertTrue(poaService.getById("1").isEmpty());
    }

    @Test
    public void entityFound() {
        when(poaRepository.findById("1")).thenReturn(Optional.of(new Poa()));

        assertTrue(poaService.getById("1").isPresent());
    }

}