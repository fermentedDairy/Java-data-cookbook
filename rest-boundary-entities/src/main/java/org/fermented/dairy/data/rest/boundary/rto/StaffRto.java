package org.fermented.dairy.data.rest.boundary.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.fermented.dairy.data.rest.entity.jpa.Staff}
 */
@AllArgsConstructor
@Getter
public class StaffRto implements Serializable {
    private final UUID id;
    private final String name;
    private final String surname;
    private final BookstoreRto bookstore;
}