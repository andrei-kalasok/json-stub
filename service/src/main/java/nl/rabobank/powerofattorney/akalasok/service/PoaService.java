package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.mapping.AbstractMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.Poa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class PoaService extends AbstractService<Poa, PoaDto> {

    @Autowired
    public PoaService(CrudRepository<Poa, String> repository, AbstractMapper<Poa, PoaDto> mapper) {
        super(repository, mapper);
    }
}
