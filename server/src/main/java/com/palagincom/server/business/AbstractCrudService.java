package com.palagincom.server.business;

import com.palagincom.server.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<K,E> {


    protected final JpaRepository<E,K> repository;
    public AbstractCrudService(JpaRepository<E,K> repository) {
        this.repository = repository;
    }





    public void create(E entity) throws EntityStateException {
        if(exists(entity)) throw new EntityStateException(entity);
        repository.save(entity);

    }
    public List<E> findAll(){
        return repository.findAll();
    }

    public void deleteById(K id ){
        repository.deleteById(id);
    }
    public void update(E entity){
        repository.save(entity);
    }
    public Optional<E> findById(K id){
        return repository.findById(id);
    }

    protected abstract boolean exists(E entity);
}