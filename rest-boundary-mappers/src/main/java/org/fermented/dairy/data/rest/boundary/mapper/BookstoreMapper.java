package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookstoreMapper {

    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "id", ignore = true)
    Bookstore toEntity(BookstoreRequestRto bookstoreRequestRto);

    BookstoreRto toRto(Bookstore bookstore);
}