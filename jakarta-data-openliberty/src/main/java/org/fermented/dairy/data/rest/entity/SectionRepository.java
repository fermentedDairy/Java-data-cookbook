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

    @Query("""
            SELECT Section s where s.bookstore.id = :bookstoreId
            """)
    Stream<Section> findByBookstoreJPQL(@Param("bookstoreId") UUID bookstoreId);

    Stream<Section> findByBookstore_id(@Param("bookstoreId") UUID bookstoreId);//NOSONAR: naming convention for proxy generation
}
