package org.fermented.dairy.data.rest.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.Bookstore;

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
        deleteById(Bookstore.class, id);
    }

    @Override
    public boolean existsById(final UUID id) {
        return em.createQuery("SELECT o.id FROM Bookstore o WHERE o.id=:bookstoreId")
                    .setParameter("bookstoreId", id)
                    .getResultStream().findFirst().isPresent();
    }

    @Override
    protected EntityManager em() {
        return em;
    }
}
