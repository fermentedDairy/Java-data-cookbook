package org.fermented.dairy.data.rest.it;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.port;


//@ExtendWith(ITEnvironmentExtension.class)
class RestApplicationIT extends ITBase{

    @BeforeAll
    static void setRestPorts() {
        port = 8080;
        healthUrl = "/data/q/health";
    }


}