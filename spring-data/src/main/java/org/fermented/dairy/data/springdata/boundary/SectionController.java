package org.fermented.dairy.data.springdata.boundary;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.mapper.SectionMapper;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.fermented.dairy.data.springdata.boundary.exceptions.NotFoundException;
import org.fermented.dairy.data.springdata.entity.BookstoreRepository;
import org.fermented.dairy.data.springdata.entity.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Stream;

@RestController
@RequestMapping("/bookstores/{bookstoreId}/sections")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SectionController {

    public static final Supplier<NotFoundException> SECTION_NOT_FOUND = () -> new NotFoundException("Section not found");
    public static final Supplier<NotFoundException> CANNOT_FIND_PARENT_BOOKSTORE = () -> new NotFoundException("Cannot find parent bookstore");

    @NonNull
    private final SectionRepository sectionRepository;

    @NonNull
    private final BookstoreRepository bookstoreRepository;

    @NonNull
    private final SectionMapper sectionMapper;

    @NonNull
    private final BookstoreMapper bookstoreMapper;


    @GetMapping
    @Transactional(readOnly = true)
    public List<SectionRto> getSections(@PathVariable("bookstoreId") final UUID bookstoreId){
        try (final Stream<Section> sections = sectionRepository.findByBookstoreId(bookstoreId)) {
            return sections.map(section -> sectionMapper.toRto(section,
                    bookstoreMapper.toRto(bookstoreRepository.findById(bookstoreId).orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE)))
            ).toList();
        }
    }

    @GetMapping("{sectionId}")
    public SectionRto getSection( @PathVariable("bookstoreId") final UUID bookstoreId,
                                  @PathVariable("sectionId") final UUID sectionId){
        return sectionRepository
                .findById(sectionId)
                .filter(section -> bookstoreId.equals(section.getBookstoreId()))
                .map(section -> sectionMapper.toRto(section,
                        bookstoreMapper.toRto(bookstoreRepository.findById(bookstoreId).orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE)))
                )
                .orElseThrow(SECTION_NOT_FOUND);
    }

    @PostMapping
    public CreateResponse<UUID> createSection( @PathVariable("bookstoreId") final UUID bookstoreId,
                                               @RequestBody final SectionRequestRto sectionRequestRto) {
        if (!bookstoreRepository.existsById(bookstoreId))
            throw CANNOT_FIND_PARENT_BOOKSTORE.get();

        final UUID sectionUUID = UUID.randomUUID();

        sectionRepository.save(sectionMapper.toEntity(sectionRequestRto)
                .toBuilder()
                .id(sectionUUID)
                .bookstoreId(bookstoreId)
                .build());
        return new CreateResponse<>(sectionUUID);
    }

    @PutMapping("{sectionId}")
    public CreateResponse<UUID> updateSection( @PathVariable("sectionId") final UUID sectionId,
                                               @RequestBody final SectionRequestRto sectionRequestRto  ) {
        return new CreateResponse<>(
                sectionRepository.save(
                        sectionRepository.findById(sectionId)
                                .map(
                                        section -> {
                                            section.setName(sectionRequestRto.getName());
                                            section.setNonFiction(sectionRequestRto.getNonFiction());
                                            return section;
                                        }
                                ).orElseThrow(SECTION_NOT_FOUND)
                ).getId()
        );
    }

    @DeleteMapping("{sectionId}")
    public CreateResponse<UUID> deleteBookstore( @PathVariable("sectionId") final UUID sectionId){
        sectionRepository.deleteById(sectionId);
        return new CreateResponse<>(sectionId);
    }
}
