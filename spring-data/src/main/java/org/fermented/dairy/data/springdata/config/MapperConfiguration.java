package org.fermented.dairy.data.springdata.config;

import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.mapper.SectionMapper;
import org.fermented.dairy.data.rest.boundary.mapper.StaffMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public BookstoreMapper getBookstoreMapper() {
        return Mappers.getMapper( BookstoreMapper.class );
    }

    @Bean
    public SectionMapper getSectionMapper() {
        return Mappers.getMapper( SectionMapper.class );
    }

    @Bean
    public StaffMapper getStaffMapper(){
        return Mappers.getMapper( StaffMapper.class );
    }
}
