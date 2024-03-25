package org.fermented.dairy.data.rest.boundary.rto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.fermented.dairy.data.rest.entity.jpa.Section}
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class SectionRto implements Serializable {
    private UUID id;
    private String name;
    private Boolean nonFiction;
    private BookstoreRto bookstore;
}