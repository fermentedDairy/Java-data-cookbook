package org.fermented.dairy.data.rest.boundary.mapper;

import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.mapstruct.Mapper;

@Mapper
public interface BookstoreMapper {
    Bookstore toEntity(BookstoreRto bookstoreRto);

    Bookstore toEntity(BookstoreRequestRto bookstoreRto);

    BookstoreRto toRto(Bookstore bookstore);
}