package org.fermented.dairy.data.rest.boundary;

import jakarta.inject.Inject;
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
import lombok.AllArgsConstructor;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.entity.BookstoreRepository;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Path("/bookstores")
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookstoreResource {

    private static final Supplier<NotFoundException> BOOKSTORE_NOT_FOUND = () -> new NotFoundException("Bookstore not found");

    private final BookstoreRepository bookstoreRepository;
    private final BookstoreMapper bookstoreMapper;

    @GET
    public List<BookstoreRto> getBookStores() {
        try (final Stream<Bookstore> bookstoreStream = bookstoreRepository.findAll()) {
            return bookstoreStream.map(bookstoreMapper::toRto).toList();
        }
    }

    @GET
    @Path("{id}")
    public BookstoreRto getBookStore(@PathParam("id") UUID id) {
        return bookstoreRepository.findById(id)
                .map(bookstoreMapper::toRto)
                .orElseThrow(BOOKSTORE_NOT_FOUND);
    }

    @POST
    public CreateResponse<UUID> createBookstore(final BookstoreRequestRto bookstoreRequestRto) {
        return new CreateResponse<>(
                bookstoreRepository.save(
                        bookstoreMapper.toEntity(bookstoreRequestRto)
                                .toBuilder()
                                .id(UUID.randomUUID())
                                .build()
                ).getId()
        );
    }

    @PUT
    @Path("{bookstoreId}")
    public CreateResponse<UUID> updateBookstore(@PathParam("bookstoreId") UUID bookstoreId,
                                                final BookstoreRequestRto bookstoreRequestRto
                                          ) {
        return new CreateResponse<>(
                bookstoreRepository.save(
                        bookstoreRepository.findById(bookstoreId).orElseThrow(BOOKSTORE_NOT_FOUND)
                                .toBuilder()
                                .name(bookstoreRequestRto.getName())
                                .address(bookstoreRequestRto.getAddress())
                                .build()
                ).getId()
        );
    }

    @DELETE
    @Path("/{id}")
    public CreateResponse<UUID> deleteBookstore(@PathParam("id") UUID id){
        bookstoreRepository.deleteById(id);
        return new CreateResponse<>(id);
    }
}
