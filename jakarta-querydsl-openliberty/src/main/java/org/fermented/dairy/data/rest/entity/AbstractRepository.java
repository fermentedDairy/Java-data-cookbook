package org.fermented.dairy.data.rest.entity;

import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRepository<E, I> implements Repository<E, I> {

    protected abstract EntityManager em();


    protected Optional<E> findById(final Class<E> target, final I id) {
        return Optional.ofNullable(em().find(target, id));
    }

    protected E save(final E entity, final Function<E, I> idFetcher) {
        if (existsById(idFetcher.apply(entity))) em().merge(entity);
        else em().persist(entity);
        return entity;
    }

}
