package org.fermented.dairy.data.rest.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.fermented.dairy.data.rest.boundary.mapper.StaffMapper;
import org.fermented.dairy.data.rest.boundary.rto.CreateResponse;
import org.fermented.dairy.data.rest.boundary.rto.StaffRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.StaffRto;
import org.fermented.dairy.data.rest.entity.BookstoreRepository;
import org.fermented.dairy.data.rest.entity.StaffRepository;
import org.fermented.dairy.data.rest.entity.jpa.Staff;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Path("/bookstores/{bookstoreId}/staff")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StaffResource {

    public static final Supplier<NotFoundException> CANNOT_FIND_PARENT_BOOKSTORE = () -> new NotFoundException("Cannot find parent bookstore");

    @Setter(onMethod = @__({@PathParam("bookstoreId")}))
    private UUID bookstoreId;

    @NonNull
    private final StaffRepository staffRepository;

    @NonNull final BookstoreRepository bookstoreRepository;

    @NonNull
    private final StaffMapper staffMapper;

    @GET
    public List<StaffRto> getStaff() {
        try (final Stream<Staff> staff = staffRepository.findByBookstore_id(bookstoreId))
        {
            return staff.map(staffMapper::toRto).toList();
        }
    }

    @POST
    public CreateResponse<UUID> onboardStaff(final StaffRequestRto staffRequestRto) {
        return new CreateResponse<>(
                staffRepository.save(
                        staffMapper.toEntity(staffRequestRto)
                                .toBuilder()
                                .id(UUID.randomUUID())
                                .bookstore(bookstoreRepository.findById(bookstoreId).orElseThrow(CANNOT_FIND_PARENT_BOOKSTORE))
                                .build()
                ).getId()
        );
    }
}
