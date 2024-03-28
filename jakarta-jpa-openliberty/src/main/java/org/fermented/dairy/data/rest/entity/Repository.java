package org.fermented.dairy.data.rest.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Repository<E, I> {
    Optional<E> findById(I id);

    Stream<E> findAll();

    E save(E entity);

    void deleteById(UUID id);

    boolean existsById(I id);
}
