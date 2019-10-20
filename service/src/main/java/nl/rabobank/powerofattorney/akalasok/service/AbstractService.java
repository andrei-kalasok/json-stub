package nl.rabobank.powerofattorney.akalasok.service;

import nl.rabobank.powerofattorney.akalasok.mapping.AbstractMapper;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class AbstractService<Entity, Dto> {

    private final CrudRepository<Entity, String> repository;
    private final AbstractMapper<Entity, Dto> mapper;

    public AbstractService(CrudRepository<Entity, String> repository, AbstractMapper<Entity, Dto> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Optional<Dto> getById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }
}
