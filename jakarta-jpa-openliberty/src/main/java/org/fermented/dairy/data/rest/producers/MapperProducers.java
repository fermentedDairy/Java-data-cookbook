package org.fermented.dairy.data.rest.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.mapper.SectionMapper;
import org.fermented.dairy.data.rest.boundary.mapper.StaffMapper;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class MapperProducers {

    @Produces
    @ApplicationScoped
    public BookstoreMapper getBookstoreMapper() {
        return Mappers.getMapper( BookstoreMapper.class );
    }

    @Produces
    @ApplicationScoped
    public SectionMapper getSectionMapper() {
        return Mappers.getMapper( SectionMapper.class );
    }

    @Produces
    @ApplicationScoped
    public StaffMapper getStaffMapper(){
        return Mappers.getMapper( StaffMapper.class );
    }
}
