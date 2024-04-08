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

@NamedQuery(name = "Section.findByBookstoreId",
        query = "select s from Section s where s.bookstore.id = :id")
@Entity
@Table(name = "section")
public class Section extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "non_fiction")
    private Boolean nonFiction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    private Bookstore bookstore;

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getNonFiction() {
        return this.nonFiction;
    }

    public Bookstore getBookstore() {
        return this.bookstore;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNonFiction(Boolean nonFiction) {
        this.nonFiction = nonFiction;
    }

    public void setBookstore(Bookstore bookstore) {
        this.bookstore = bookstore;
    }

    //<editor-fold desc="panache methods">
    public static Stream<Section> findByBookstoreId(final UUID bookstoreId) {
        return stream("#Section.findByBookstoreId", Parameters.with("id", bookstoreId).map());
    }
    //</editor-fold>
}