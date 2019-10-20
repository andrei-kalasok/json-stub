package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
import nl.rabobank.powerofattorney.akalasok.mapping.AbstractMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class DebitCardService extends AbstractService<DebitCard, DebitCardDto> {

    @Autowired
    public DebitCardService(CrudRepository<DebitCard, String> repository, AbstractMapper<DebitCard, DebitCardDto> mapper) {
        super(repository, mapper);
    }
}
