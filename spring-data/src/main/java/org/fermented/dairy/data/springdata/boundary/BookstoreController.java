package org.fermented.dairy.data.springdata.boundary;

import lombok.AllArgsConstructor;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.springdata.boundary.exceptions.NotFoundException;
import org.fermented.dairy.data.springdata.entity.BookstoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping(value = "bookstores")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookstoreController {

    private static final Supplier<NotFoundException> BOOKSTORE_NOT_FOUND = () -> new NotFoundException("Bookstore not found");


    private final BookstoreRepository bookstoreRepository;
    private final BookstoreMapper bookstoreMapper;

    @GetMapping
    public List<BookstoreRto> getBookStores() {
            return bookstoreRepository.findAll().stream().map(bookstoreMapper::toRto).toList();
    }

    @GetMapping("/{id}")
    public BookstoreRto getBookStore(@PathVariable("id") UUID id) {
        return bookstoreRepository.findById(id)
                .map(bookstoreMapper::toRto)
                .orElseThrow(BOOKSTORE_NOT_FOUND);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public CreateResponse<UUID> createBookstore(@RequestBody final BookstoreRequestRto bookstoreRequestRto) {
        return new CreateResponse<>(
                bookstoreRepository.save(
                        bookstoreMapper.toEntity(bookstoreRequestRto)
                                .toBuilder()
                                .id(UUID.randomUUID())
                                .build()
                ).getId()
        );
    }

    @PutMapping(value = "{bookstoreId}", consumes = "application/json", produces = "application/json")
    public CreateResponse<UUID> updateBookstore(@PathVariable("bookstoreId") UUID bookstoreId,
                                                @RequestBody final BookstoreRequestRto bookstoreRequestRto
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

    @DeleteMapping("/{id}")
    public CreateResponse<UUID> deleteBookstore(@PathVariable("id") UUID id){
        bookstoreRepository.deleteById(id);
        return new CreateResponse<>(id);
    }
}
