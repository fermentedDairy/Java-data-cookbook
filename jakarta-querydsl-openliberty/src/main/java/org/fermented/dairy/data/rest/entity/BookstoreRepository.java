package org.fermented.dairy.data.rest.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;
import org.fermented.dairy.data.rest.entity.jpa.QBookstore;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class BookstoreRepository extends AbstractRepository<Bookstore, UUID> {

    private static final QBookstore bookstore = QBookstore.bookstore;

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @PersistenceContext(name = "jpa-unit")
    public void setEntityManager(final EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Bookstore> findById(final UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(bookstore).where(bookstore.id.eq(id)).fetchOne()
        );
    }

    @Override
    public Stream<Bookstore> findAll() {
        return queryFactory.selectFrom(bookstore).stream();
    }

    @Override
    public Bookstore save(final Bookstore bookstore) {
        return save(bookstore, Bookstore::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        queryFactory.delete(bookstore).where(bookstore.id.eq(id)).execute();
    }

    @Override
    public boolean existsById(final UUID id) {
        return queryFactory.selectFrom(bookstore).where(bookstore.id.eq(id)).stream().findFirst().isPresent();
    }

    @Override
    protected EntityManager em() {
        return em;
    }

}
