package org.fermented.dairy.data.rest.entity.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.util.UUID;
import java.util.stream.Stream;

@NamedQuery(name = "Staff.findByBookstoreId",
        query = "select s from Staff s where s.bookstore.id = :id")
@Entity
@Table(name = "staff")
public class Staff extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "name", nullable = false, length = 1024)
    private String name;

    @Column(name = "surname", nullable = false, length = 1024)
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bookstore_id", nullable = false)
    private Bookstore bookstore;

    //<editor-fold desc="getters">
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Bookstore getBookstore() {
        return this.bookstore;
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public void setId(final UUID id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setBookstore(final Bookstore bookstore) {
        this.bookstore = bookstore;
    }
    //</editor-fold>

    //<editor-fold desc="panache methods">
    public static Stream<Staff> findByBookstoreId(final UUID bookstoreId) {
        return stream("#Section.findByBookstoreId", Parameters.with("id", bookstoreId).map());
    }
    //</editor-fold>
}