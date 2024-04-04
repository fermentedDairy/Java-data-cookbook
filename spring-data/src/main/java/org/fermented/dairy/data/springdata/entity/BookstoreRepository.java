package org.fermented.dairy.data.springdata.entity;

import java.util.UUID;

import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.springframework.data.repository.ListCrudRepository;

public interface BookstoreRepository extends ListCrudRepository<Bookstore, UUID> {
}
