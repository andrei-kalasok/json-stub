package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.mapping.AbstractMapper;
import nl.rabobank.powerofattorney.akalasok.persistence.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService extends AbstractService<CreditCard, CreditCardDto> {

    @Autowired
    public CreditCardService(CrudRepository<CreditCard, String> repository, AbstractMapper<CreditCard, CreditCardDto> mapper) {
        super(repository, mapper);
    }
}
