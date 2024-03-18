package org.fermented.dairy.data.rest.entity;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.Param;
import jakarta.data.repository.Repository;
import org.fermented.dairy.data.rest.entity.jpa.Staff;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface StaffRepository extends CrudRepository<Staff, UUID> {

    //generates SELECT ID, name, surname, bookstore_id FROM Staff WHERE (bookstore_id = ?) ORDER BY name DESC
    @OrderBy(value = "name", descending = true)
    Stream<Staff> findByBookstore_id(UUID id);//NOSONAR: java:S100, naming convention required to generate required SQL

    //generates JPQL: SELECT o FROM Staff o WHERE (o.name LIKE ?1)
    Stream<Staff> findByNameLike(@Param("name") String memberName);
}
