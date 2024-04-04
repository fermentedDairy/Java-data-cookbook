package org.fermented.dairy.data.rest.entity;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;

import java.util.UUID;

@Repository
public interface BookstoreRepository extends CrudRepository<Bookstore, UUID> {
    //findAll generates SELECT ID, ADDRESS, NAME FROM Bookstore
    //findById generates SELECT ID, ADDRESS, NAME FROM Bookstore WHERE (ID = ?)
    //save generates INSERT INTO Bookstore (ID, ADDRESS, NAME) VALUES (?, ?, ?)

}
