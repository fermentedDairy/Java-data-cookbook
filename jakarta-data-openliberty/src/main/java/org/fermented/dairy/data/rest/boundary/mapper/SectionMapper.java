package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI, uses = {BookstoreMapper.class})
public interface SectionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookstore", ignore = true)
    Section toEntity(SectionRequestRto sectionRequestRto);

    SectionRto toRto(Section section);

}