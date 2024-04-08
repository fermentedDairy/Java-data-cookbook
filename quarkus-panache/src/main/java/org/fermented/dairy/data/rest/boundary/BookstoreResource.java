package org.fermented.dairy.data.rest.boundary;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Path("/bookstores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class BookstoreResource {

    private static final Supplier<NotFoundException> BOOKSTORE_NOT_FOUND = () -> new NotFoundException("Bookstore not found");

    private final BookstoreMapper bookstoreMapper =Mappers.getMapper(BookstoreMapper.class);

    @GET
    public List<BookstoreRto> getBookStores() {
        try (final Stream<Bookstore> bookstoreStream = Bookstore.streamAll()) {
            return bookstoreStream.map(bookstoreMapper::toRto).toList();
        }
    }

    @GET
    @Path("{id}")
    public BookstoreRto getBookStore(@PathParam("id") final UUID id) {
        final Optional<Bookstore> bookstoreOptional = Bookstore.findByIdOptional(id);

        return bookstoreOptional.map(bookstoreMapper::toRto)
                .orElseThrow(BOOKSTORE_NOT_FOUND);
    }

    @POST
    public CreateResponse<UUID> createBookstore(final BookstoreRequestRto bookstoreRequestRto) {
        final Bookstore bookstore = bookstoreMapper.toEntity(bookstoreRequestRto);
        bookstore.setId(UUID.randomUUID());
        Bookstore.persist(bookstore);
        return new CreateResponse<>(bookstore.getId());
    }

    @PUT
    @Path("{bookstoreId}")
    public CreateResponse<UUID> updateBookstore(@PathParam("bookstoreId") final UUID bookstoreId,
                                                final BookstoreRequestRto bookstoreRequestRto
                                          ) {


        final Optional<Bookstore> bookstoreOptional = Bookstore.findByIdOptional(bookstoreId);
        final Bookstore bookstore = bookstoreOptional.orElseThrow(BOOKSTORE_NOT_FOUND);

        bookstore.setAddress(bookstoreRequestRto.getAddress());
        bookstore.setName(bookstoreRequestRto.getName());

        Bookstore.persist(bookstore);
        return new CreateResponse<>(bookstore.getId());
    }

    @DELETE
    @Path("/{id}")
    public CreateResponse<UUID> deleteBookstore(@PathParam("id") final UUID id){
        Bookstore.deleteById(id);
        return new CreateResponse<>(id);
    }
}
