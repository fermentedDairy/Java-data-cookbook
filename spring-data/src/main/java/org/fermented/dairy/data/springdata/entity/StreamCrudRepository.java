package org.fermented.dairy.data.springdata.entity;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@NoRepositoryBean
public interface StreamCrudRepository<T, ID> extends Repository<T, ID> {

    Stream<T> findAll();

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);

    void deleteById(ID id);
}
