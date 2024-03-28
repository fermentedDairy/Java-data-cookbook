package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.StaffRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.StaffRto;
import org.fermented.dairy.data.rest.entity.jpa.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI, uses = {BookstoreMapper.class})
public interface StaffMapper {
    Staff toEntity(StaffRto staffRto);

    Staff toEntity(StaffRequestRto staffRto);

    StaffRto toRto(Staff staff);

}