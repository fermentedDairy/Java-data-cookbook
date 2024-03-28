package org.fermented.dairy.data.rest.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.Staff;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class StaffRepository extends AbstractRepository<Staff, UUID> {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    @Override
    public Optional<Staff> findById(final UUID id) {
        return findById(Staff.class, id);
    }

    @Override
    public Stream<Staff> findAll() {
        return em.createQuery("SELECT S from Staff s", Staff.class).getResultStream();
    }

    @Override
    public Staff save(final Staff staff) {
        return save(staff, Staff::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        deleteById(Staff.class, id);
    }

    @Override
    public boolean existsById(final UUID id) {
        return em.createQuery("SELECT o.id FROM Staff o WHERE o.id=:id")
                .setParameter("id", id)
                .getResultStream().findFirst().isPresent();
    }

    public Stream<Staff> findByBookstoreId(final UUID bookstoreId) {
        return em.createQuery(
                    """
                            SELECT S from Staff S where S.bookstore.id = :bookstoreId
                    """,
                    Staff.class)
                .setParameter("bookstoreId", bookstoreId)
                .getResultStream();
    }

    @Override
    protected EntityManager em() {
        return em;
    }
}
