package org.fermented.dairy.data.rest.entity;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import org.fermented.dairy.data.rest.entity.jpa.Section;

import java.util.UUID;
import java.util.stream.Stream;


@Repository
public interface SectionRepository extends CrudRepository<Section, UUID> {
    /*
    findById issue: https://github.com/OpenLiberty/open-liberty/issues/27925
     */


    //Generates SELECT ID, bookstore_id, name, non_fiction FROM Section WHERE (bookstore_id = ?)
    @Query("""
            SELECT s from Section s where s.bookstoreId = :bookstoreId
            """)
    Stream<Section> findByBookstoreJPQL(@Param("bookstoreId") UUID bookstoreId);

    //generates SELECT ID, bookstore_id, name, non_fiction FROM Section WHERE (bookstore_id = ?)
    Stream<Section> findByBookstoreId(@Param("bookstoreId") UUID bookstoreId);

}
