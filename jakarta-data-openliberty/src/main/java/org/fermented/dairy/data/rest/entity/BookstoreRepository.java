package org.fermented.dairy.data.rest.entity;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;

import java.util.UUID;

@Repository
public interface BookstoreRepository extends CrudRepository<Bookstore, UUID> {
}
