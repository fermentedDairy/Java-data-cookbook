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

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Override
    public Optional<Bookstore> findById(final UUID id) {
        return findById(Bookstore.class, id);
    }

    @Override
    public Stream<Bookstore> findAll() {
        return em.createQuery("SELECT B from Bookstore B", Bookstore.class).getResultStream();
    }

    @Override
    public Bookstore save(final Bookstore bookstore) {
        return save(bookstore, Bookstore::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        final QBookstore bookstore = QBookstore.bookstore;
        final JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(bookstore).where(bookstore.id.eq(id)).execute();
    }

    @Override
    public boolean existsById(final UUID id) {
        final QBookstore bookstore = QBookstore.bookstore;
        final JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(bookstore).where(bookstore.id.eq(id)).stream().findFirst().isPresent();
    }

    @Override
    protected EntityManager em() {
        return em;
    }

}
