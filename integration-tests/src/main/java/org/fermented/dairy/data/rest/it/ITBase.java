package org.fermented.dairy.data.rest.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.BookstoreRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRequestRto;
import org.fermented.dairy.data.rest.boundary.rto.SectionRto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ITBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final List<UUID> bookstoreIds = new ArrayList<>();

    private final List<UUIDPair> sectionIds = new ArrayList<>();

    protected String healthUrl = "/health/";

    @AfterEach
    void cleanup(){

        for(final Iterator<UUIDPair> it = sectionIds.iterator(); it.hasNext(); ) {
            final UUIDPair id = it.next();
            given()
                    .pathParams(Map.of(
                            "bookstoreID", id.left,
                            "sectionId", id.right
                    ))
                    .when()
                    .delete("/data/bookstores/{bookstoreID}/sections/{sectionId}")
                    .then()
                    .statusCode(200);
            it.remove();
        }

        for(final Iterator<UUID> it = bookstoreIds.iterator(); it.hasNext(); ) {
            final UUID id = it.next();
            given()
                    .pathParams("bookstoreID", id)
                    .when()
                    .delete("/data/bookstores/{bookstoreID}")
                    .then()
                    .statusCode(200);
            it.remove();
        }

    }

    @Test
    @DisplayName("Calls to health endpoints return 200")
    void healthEndpointReturn200() {
        when()
                .get(healthUrl)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test bookstore CRUD operations")
    void bookstoreCrudTest() throws IOException {
        //Validate Empty Response
        when()
                .get("/data/bookstores")
                .then()
                .statusCode(200)
                .body("", equalTo(Collections.emptyList()));

        final BookstoreRto bookstore1 = createBookstore("Bookstore 1", "Bookstore 1 street");
        final BookstoreRto bookstore2 = createBookstore("Bookstore2", "Bookstore 2 street");

        //Validate fetching all bookstores
        final List<BookstoreRto> bookstores = when()
                .get("/data/bookstores")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", BookstoreRto.class);

        assertEquals(List.of(bookstore1, bookstore2), bookstores, "Bookstore list incorrect");


        //edit bookstore
        final String id = given()
                .header("Content-type", "application/json")
                .body(mapper.writeValueAsString(BookstoreRequestRto.builder()
                        .address(bookstore2.getAddress())
                        .name("Bookstore 2")
                        .build()
                ))
                .pathParam("bookstoreId", bookstore2.getId())
                .when()
                .put("/data/bookstores/{bookstoreId}")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertEquals(bookstore2.getId(), UUID.fromString(id), "Id is incorrect");

        final BookstoreRto editedBookstore = given()
                .pathParam("bookstoreID", bookstore2.getId())
                .when()
                .get("/data/bookstores/{bookstoreID}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .as(BookstoreRto.class);

        assertEquals(bookstore2.toBuilder().name("Bookstore 2").build(), editedBookstore, "Bookstore has not been edited correctly");

    }

    @Test
    @DisplayName("Test section CRUD operations")
    void sectionCrudTest() throws JsonProcessingException {

        final BookstoreRto bookstore1 = createBookstore("section owning", "another address");
        final BookstoreRto bookstore2 = createBookstore("not section owning", "another address");

        //Validate Empty Response
        given()
                .pathParam("bookstoreId", bookstore1.getId())
                .when()
                .get("/data/bookstores/{bookstoreId}/sections")
                .then()
                .statusCode(200)
                .body("", equalTo(Collections.emptyList()));

        final SectionRto section1 = createSection("Sci-Fi", false, bookstore1);
        final SectionRto section2 = createSection("Physics", true, bookstore1);

        final List<SectionRto> sections = given()
                .pathParam("bookstoreId", bookstore1.getId())
                .when()
                .get("/data/bookstores/{bookstoreId}/sections")
                .then()
                .statusCode(200).extract()
                .jsonPath().getList(".", SectionRto.class);

        //not leaking other bookstores sections
        given()
                .pathParams(Map.of(
                        "bookstoreId", bookstore2.getId(),
                        "sectionId", section1.getId()
                ))
                .when()
                .get("/data/bookstores/{bookstoreId}/sections/{sectionId}")
                .then()
                .statusCode(404);

        assertEquals(List.of(section1, section2), sections, "Section list incorrect");

        //edit bookstore
        final String id = given()
                .header("Content-type", "application/json")
                .body(mapper.writeValueAsString(SectionRto.builder()
                        .name(section1.getName() + " edited")
                        .nonFiction(section1.getNonFiction())
                        .build()
                ))
                .pathParams(Map.of(
                        "bookstoreId", bookstore1.getId(),
                        "sectionId", section1.getId()
                ))
                .when()
                .put("/data/bookstores/{bookstoreId}/sections/{sectionId}")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertEquals(section1.getId(), UUID.fromString(id), "Id is incorrect");

        final SectionRto editedSection = given()
                .pathParams(Map.of(
                        "bookstoreID", bookstore1.getId(),
                        "sectionId", section1.getId()
                ))
                .when()
                .get("/data/bookstores/{bookstoreID}/sections/{sectionId}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .as(SectionRto.class);

        assertEquals(section1.toBuilder().name(section1.getName() + " edited").build(),
                editedSection, "Section has not been edited correctly");

    }

    private SectionRto createSection(final String name,
                                     final Boolean nonFiction,
                                     final BookstoreRto bookstore) throws JsonProcessingException {
        final SectionRequestRto sectionRequestRto = SectionRequestRto.builder()
                .name(name)
                .nonFiction(nonFiction)
                .build();

        final String sectionId = given()
                .header("Content-type", "application/json")
                .body(mapper.writeValueAsString(sectionRequestRto))
                .pathParam("bookstoreId", bookstore.getId())
                .when()
                .post("/data/bookstores/{bookstoreId}/sections")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertEquals(UUID.fromString(sectionId).toString(), sectionId, "UUID is not valid");

        sectionIds.add(new UUIDPair(bookstore.getId(), UUID.fromString(sectionId)));

        final SectionRto expected = SectionRto.builder()
                .bookstore(bookstore)
                .id(UUID.fromString(sectionId))
                .nonFiction(sectionRequestRto.getNonFiction())
                .name(sectionRequestRto.getName())
                .build();

        final SectionRto actual = given()
                .pathParams(Map.of(
                        "bookstoreID", bookstore.getId(),
                        "sectionId", expected.getId()
                ))
                .when()
                .get("/data/bookstores/{bookstoreID}/sections/{sectionId}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .as(SectionRto.class);

        assertEquals(expected, actual, "Section incorrect");
        return actual;
    }

    private BookstoreRto createBookstore(final String name, final String address) throws JsonProcessingException {
        final BookstoreRequestRto bookstoreRequest = BookstoreRequestRto.builder()
                .name(name)
                .address(address)
                .build();

        final String id1 = given()
                .header("Content-type", "application/json")
                .body(mapper.writeValueAsString(bookstoreRequest))
                .when()
                .post("/data/bookstores")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertEquals(UUID.fromString(id1).toString(), id1, "UUID is not valid");

        final BookstoreRto expected = BookstoreRto.builder()
                .address(bookstoreRequest.getAddress())
                .name(bookstoreRequest.getName())
                .id(UUID.fromString(id1))
                .build();

        bookstoreIds.add(expected.getId());

        final BookstoreRto responseValue = given()
                .pathParam("bookstoreID", expected.getId())
                .when()
                .get("/data/bookstores/{bookstoreID}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .as(BookstoreRto.class);

        assertEquals(expected, responseValue, "Bookstore incorrect");

        return responseValue;

    }

    private record UUIDPair(UUID left, UUID right){}
}
