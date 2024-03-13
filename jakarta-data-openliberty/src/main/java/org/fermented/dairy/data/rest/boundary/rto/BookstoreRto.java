package org.fermented.dairy.data.rest.boundary.rto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class BookstoreRto implements Serializable {
    private final UUID id;
    private final String name;
    private final String address;
}