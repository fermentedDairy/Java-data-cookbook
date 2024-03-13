package org.fermented.dairy.data.rest.entity.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
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
    UUID id = UUID.randomUUID();

    String name;

    String address;

    @OneToMany(mappedBy = "bookstore", cascade= CascadeType.ALL)
    private Set<Section> sections;

    public void addSection(Section section) {
        sections.add(section);
        section.setBookstore(this);
    }

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
