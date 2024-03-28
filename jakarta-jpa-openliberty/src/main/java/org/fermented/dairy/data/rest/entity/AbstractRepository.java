package org.fermented.dairy.data.rest.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRepository<E, I> implements Repository<E, I> {

    protected abstract EntityManager em();

    protected Optional<E> findById(Class<E> target, I id) {
        return Optional.ofNullable(em().find(target, id));
    }

    protected E save(E entity, Function<E, I> idFetcher) {
        if (existsById(idFetcher.apply(entity))) em().merge(entity);
        else em().persist(entity);
        return entity;
    }

    protected void deleteById(Class<E> target, I id) {
        final CriteriaBuilder builder = em().getCriteriaBuilder();
        final CriteriaDelete<E> criteria = builder.createCriteriaDelete(target);
        Root<E> root = criteria.from(target);
        criteria.where(builder.equal(root.get("id"), id));

        em().createQuery(criteria).executeUpdate();
    }

}
