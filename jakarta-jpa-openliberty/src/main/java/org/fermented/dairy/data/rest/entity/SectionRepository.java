package org.fermented.dairy.data.rest.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.Section;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class SectionRepository extends AbstractRepository<Section, UUID> {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Override
    public Optional<Section> findById(final UUID id) {
        return findById(Section.class, id);
    }

    @Override
    public Stream<Section> findAll() {
        return em.createQuery("SELECT S from Section S", Section.class).getResultStream();
    }

    @Override
    public Section save(final Section section) {
        return save(section, Section::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        deleteById(Section.class, id);
    }

    @Override
    public boolean existsById(final UUID id) {
        return em.createQuery("SELECT o.id FROM Section o WHERE o.id=:id")
                .setParameter("id", id)
                .getResultStream().findFirst().isPresent();
    }

    public Stream<Section> findByBookstoreId(final UUID bookstoreId) {
        return em.createQuery("SELECT S from Section S where S.bookstoreId = :bookstoreId", Section.class)
                .setParameter("bookstoreId", bookstoreId)
                .getResultStream();
    }

    @Override
    protected EntityManager em() {
        return em;
    }
}
