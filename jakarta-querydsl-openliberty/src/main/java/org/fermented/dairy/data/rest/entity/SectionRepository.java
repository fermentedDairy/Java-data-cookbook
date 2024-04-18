package org.fermented.dairy.data.rest.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.QSection;
import org.fermented.dairy.data.rest.entity.jpa.Section;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class SectionRepository extends AbstractRepository<Section, UUID> {

    private static final QSection section = QSection.section;

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @PersistenceContext(name = "jpa-unit")
    public void setEntityManager(final EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Section> findById(final UUID id) {
        return Optional.ofNullable(queryFactory.selectFrom(section).where(section.id.eq(id)).fetchOne());
    }

    @Override
    public Stream<Section> findAll() {
        return queryFactory.selectFrom(section).stream();
    }

    @Override
    public Section save(final Section section) {
        return save(section, Section::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        queryFactory.delete(section).where(section.id.eq(id)).execute();
    }

    @Override
    public boolean existsById(final UUID id) {
        return !queryFactory.selectFrom(section).where(section.id.eq(id)).fetch().isEmpty();
    }

    public Stream<Section> findByBookstoreId(final UUID bookstoreId) {
        return queryFactory.selectFrom(section).where(section.bookstoreId.eq(bookstoreId)).stream();
    }

    @Override
    protected EntityManager em() {
        return em;
    }
}
