package org.fermented.dairy.data.rest.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Bookstore {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String name;

    private String address;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Bookstore bookstore = (Bookstore) o;
        return getId() != null && Objects.equals(getId(), bookstore.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
