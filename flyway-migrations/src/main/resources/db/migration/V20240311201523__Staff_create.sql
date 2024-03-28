CREATE TABLE staff (
    ID UUID PRIMARY KEY,
    NAME VARCHAR(1024) NOT NULL ,
    SURNAME VARCHAR(1024) NOT NULL ,
    bookstore_id UUID NOT NULL
        CONSTRAINT STAFF_BOOKSTORE_ID_FK
            REFERENCES BOOKSTORE
)