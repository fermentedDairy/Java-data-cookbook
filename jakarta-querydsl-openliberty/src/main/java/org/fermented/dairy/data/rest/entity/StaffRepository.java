package org.fermented.dairy.data.rest.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.fermented.dairy.data.rest.entity.jpa.QStaff;
import org.fermented.dairy.data.rest.entity.jpa.Staff;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class StaffRepository extends AbstractRepository<Staff, UUID> {

    private static final QStaff staff = QStaff.staff;

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @PersistenceContext(name = "jpa-unit")
    public void setEntityManager(final EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Staff> findById(final UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(staff).fetchOne()
        );
    }

    @Override
    public Stream<Staff> findAll() {
        return queryFactory.selectFrom(staff).stream();
    }

    @Override
    public Staff save(final Staff staff) {
        return save(staff, Staff::getId);
    }

    @Override
    public void deleteById(final UUID id) {
        queryFactory.delete(staff).where(staff.id.eq(id)).execute();
    }

    @Override
    public boolean existsById(final UUID id) {
        return !queryFactory.selectFrom(staff).where(staff.id.eq(id)).fetch().isEmpty();
    }

    public Stream<Staff> findByBookstoreId(final UUID bookstoreId) {
        return queryFactory.selectFrom(staff).where(staff.bookstore.id.eq(bookstoreId)).stream();
    }

    @Override
    protected EntityManager em() {
        return em;
    }
}
