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
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.fermented.dairy.data.rest.entity.jpa.Section;
import org.fermented.dairy.data.rest.boundary.mapper.SectionMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Path("/bookstores/{bookstoreId}/sections")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class SectionResource {

    public static final Supplier<NotFoundException> SECTION_NOT_FOUND = () -> new NotFoundException("Section not found");
    public static final Supplier<NotFoundException> CANNOT_FIND_PARENT_BOOKSTORE = () -> new NotFoundException("Cannot find parent bookstore");

    @PathParam("bookstoreId")
    private UUID bookstoreId;

    private final SectionMapper sectionMapper = Mappers.getMapper(SectionMapper.class);


    @GET
    public List<SectionRto> getSections(){
        try (final Stream<Section> sections = Section.findByBookstoreId(bookstoreId)) {
            return sections.map(sectionMapper::toRto).toList();
        }
    }

    @GET
    @Path("{sectionId}")
    public SectionRto getSection(@PathParam("sectionId") final UUID sectionId){
        final Optional<Section> sectionOptional = Section
                .findByIdOptional(sectionId);
        return sectionOptional.filter(section -> bookstoreId.equals(section.getBookstore().getId()))
                .map(sectionMapper::toRto
                )
                .orElseThrow(SECTION_NOT_FOUND);
    }

    @POST
    public CreateResponse<UUID> createSection(final SectionRequestRto sectionRequestRto) {
        if (Bookstore.findByIdOptional(bookstoreId).isEmpty())
                throw CANNOT_FIND_PARENT_BOOKSTORE.get();

        final UUID sectionUUID = UUID.randomUUID();

        Section section = sectionMapper.toEntity(sectionRequestRto);

        section.setId(sectionUUID);
        section.setBookstore(Bookstore.findById(bookstoreId));

        Section.persist(section);
        return new CreateResponse<>(sectionUUID);
    }

    @PUT
    @Path("{sectionId}")
    public CreateResponse<UUID> updateSection(@PathParam("sectionId") final UUID sectionId,
                                              final SectionRequestRto sectionRequestRto  ) {
        Optional<Section> optionalSection = Section.findByIdOptional(sectionId);
        Section.persist(optionalSection.map(
                        section -> {
                            section.setName(sectionRequestRto.getName());
                            section.setNonFiction(sectionRequestRto.getNonFiction());
                            return section;
                        }
                ).orElseThrow(SECTION_NOT_FOUND)
        );
        return new CreateResponse<>(
                optionalSection.get().getId()
        );
    }

    @DELETE
    @Path("{sectionId}")
    public CreateResponse<UUID> deleteBookstore(@PathParam("sectionId") final UUID sectionId){
        Section.deleteById(sectionId);
        return new CreateResponse<>(sectionId);
    }
}
