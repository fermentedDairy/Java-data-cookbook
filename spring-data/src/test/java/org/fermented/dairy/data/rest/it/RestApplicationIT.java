package org.fermented.dairy.data.rest.it;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.port;

public class RestApplicationIT extends ITBase {


    @BeforeAll
    static void setRestPorts() {
        port = 8080;

    }

    @BeforeEach
    void setHealthURL(){
        healthUrl = "/data/health";
    }
}
