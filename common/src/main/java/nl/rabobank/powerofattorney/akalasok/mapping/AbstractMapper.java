package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.persistence.Account;
import nl.rabobank.powerofattorney.akalasok.persistence.Poa;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

public abstract class AbstractMapper<E, D> {

    private final ModelMapper modelMapper = new ModelMapper();

    public AbstractMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(new PropertyMap<Account, AccountDto>() {
            @Override
            protected void configure() {
                skip(destination.getOwner());
            }
        });
        modelMapper.addMappings(new PropertyMap<Poa, PoaDto>() {
            @Override
            protected void configure() {
                skip(destination.getCards());
            }
        });
        modelMapper.addMappings(new PropertyMap<PoaDto, Poa>() {
            @Override
            protected void configure() {
                skip(destination.getCards());
            }
        });
    }

    public E toEntity(D dto) {
        E entity = newEntity();
        modelMapper.map(dto, entity);
        customEntityMapping(dto, entity);
        return entity;
    }

    public D toDto(E entity) {
        D dto = newDto();
        modelMapper.map(entity, dto);
        customDtoMapping(entity, dto);
        return dto;
    }

    protected abstract E newEntity();

    protected abstract D newDto();

    protected void customEntityMapping(D dto, E entity) {

    }

    protected void customDtoMapping(E entity, D dto) {
    }

}
