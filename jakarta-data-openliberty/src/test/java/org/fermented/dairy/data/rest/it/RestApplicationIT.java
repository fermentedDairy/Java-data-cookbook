package org.fermented.dairy.data.rest.it;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;


//@ExtendWith(ITEnvironmentExtension.class)
class RestApplicationIT extends ITBase{

    @BeforeAll
    static void setRestPorts() {
        port = 9080;
    }


}