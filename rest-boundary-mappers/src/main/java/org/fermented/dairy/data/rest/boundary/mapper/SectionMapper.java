package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {BookstoreMapper.class})
public interface SectionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookstoreId", ignore = true)
    Section toEntity(SectionRequestRto sectionRequestRto);

    @Mapping(target = "bookstore", source = "bookstoreRto")
    @Mapping(target = "id", source = "section.id")
    @Mapping(target = "name", source = "section.name")
    SectionRto toRto(Section section, BookstoreRto bookstoreRto);

}