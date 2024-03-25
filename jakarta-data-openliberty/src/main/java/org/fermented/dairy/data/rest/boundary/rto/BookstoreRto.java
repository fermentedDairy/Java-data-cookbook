package org.fermented.dairy.data.rest.boundary.rto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class BookstoreRto implements Serializable {
    private UUID id;
    private String name;
    private String address;
}