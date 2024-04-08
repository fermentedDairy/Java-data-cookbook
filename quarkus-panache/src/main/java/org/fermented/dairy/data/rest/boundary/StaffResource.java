package org.fermented.dairy.data.rest.boundary;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.fermented.dairy.data.rest.boundary.mapper.StaffMapper;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.boundary.rto.StaffRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.StaffRto;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.fermented.dairy.data.rest.entity.jpa.Staff;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Path("/bookstores/{bookstoreId}/staff")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class StaffResource {

    public static final Supplier<NotFoundException> CANNOT_FIND_PARENT_BOOKSTORE = () -> new NotFoundException("Cannot find parent bookstore");

    @PathParam("bookstoreId")
    private UUID bookstoreId;

    private final StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    @GET
    public List<StaffRto> getStaff() {
        try (final Stream<Staff> staff = Staff.findByBookstoreId(bookstoreId))
        {
            return staff.map(staffMapper::toRto).toList();
        }
    }

    @POST
    public CreateResponse<UUID> onboardStaff(final StaffRequestRto staffRequestRto) {
        final Optional<Bookstore> bookstore = Bookstore.findByIdOptional(bookstoreId);
        final Staff staff = staffMapper.toEntity(staffRequestRto);
        staff.setId(UUID.randomUUID());
        staff.setBookstore(bookstore.orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE));
        staff.persist();
        return new CreateResponse<>(staff.getId());
    }
}
