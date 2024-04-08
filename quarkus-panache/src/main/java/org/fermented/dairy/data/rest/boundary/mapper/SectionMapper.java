package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.mapstruct.Mapper;

@Mapper
public interface SectionMapper {
    Section toEntity(SectionRto sectionRto);

    Section toEntity(SectionRequestRto sectionRto);

    SectionRto toRto(Section section);
}