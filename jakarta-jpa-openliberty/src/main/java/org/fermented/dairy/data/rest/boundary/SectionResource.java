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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.fermented.dairy.data.rest.boundary.mapper.BookstoreMapper;
import org.fermented.dairy.data.rest.boundary.mapper.SectionMapper;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.BookstoreRepository;
import org.fermented.dairy.data.rest.entity.SectionRepository;
import org.fermented.dairy.data.rest.entity.jpa.Section;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Path("/bookstores/{bookstoreId}/sections")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SectionResource {

    public static final Supplier<NotFoundException> SECTION_NOT_FOUND = () -> new NotFoundException("Section not found");
    public static final Supplier<NotFoundException> CANNOT_FIND_PARENT_BOOKSTORE = () -> new NotFoundException("Cannot find parent bookstore");

    @Setter(onMethod = @__({@PathParam("bookstoreId")}))
    private UUID bookstoreId;

    @NonNull
    private final SectionRepository sectionRepository;

    @NonNull
    private final BookstoreRepository bookstoreRepository;

    @NonNull
    private final SectionMapper sectionMapper;

    @NonNull
    private final BookstoreMapper bookstoreMapper;

    @GET
    public List<SectionRto> getSections(){
        try (Stream<Section> sections = sectionRepository.findByBookstoreId(bookstoreId)) {
            return sections.map(section -> sectionMapper.toRto(section,
                    bookstoreMapper.toRto(bookstoreRepository.findById(bookstoreId).orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE)))
            ).toList();
        }
    }

    @GET
    @Path("{sectionId}")
    public SectionRto getSection(@PathParam("sectionId") UUID sectionId){
        return sectionRepository
                .findById(sectionId)
                .filter(section -> bookstoreId.equals(section.getBookstoreId()))
                .map(section -> sectionMapper.toRto(section,
                                bookstoreMapper.toRto(bookstoreRepository.findById(bookstoreId).orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE)))
                )
                .orElseThrow(SECTION_NOT_FOUND);
    }

    @POST
    public CreateResponse<UUID> createSection(SectionRequestRto sectionRequestRto) {
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

    @PUT
    @Path("{sectionId}")
    public CreateResponse<UUID> updateSection( @PathParam("sectionId") UUID sectionId,
                                               SectionRequestRto sectionRequestRto  ) {
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

    @DELETE
    @Path("{sectionId}")
    public CreateResponse<UUID> deleteBookstore(@PathParam("sectionId") UUID sectionId){
        sectionRepository.deleteById(sectionId);
        return new CreateResponse<>(sectionId);
    }
}
