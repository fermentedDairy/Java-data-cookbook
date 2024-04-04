package org.fermented.dairy.data.springdata.entity;

import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface SectionRepository extends ListCrudRepository<Section, UUID> {
    Stream<Section> findByBookstoreId(UUID bookstoreId);
}
