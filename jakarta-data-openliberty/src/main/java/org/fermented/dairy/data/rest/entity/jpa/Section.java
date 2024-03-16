package org.fermented.dairy.data.rest.entity.jpa;

import jakarta.persistence.Column;
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
public class Section {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "non_fiction")
    private Boolean nonFiction;

    @Column(name = "bookstore_id")
    private UUID bookstoreId;

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return getId() != null && Objects.equals(getId(), section.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}