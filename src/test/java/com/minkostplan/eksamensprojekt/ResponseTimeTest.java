package com.minkostplan.eksamensprojekt;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse for at måle responstid for et endpoint.
 */
public class ResponseTimeTest {

    /**
     * Test for at måle responstid for et enkelt GET-forespørgsel.
     */
    @Test
    public void testResponseTime() {
        // URL til dit endpoint, juster dette til din faktiske URL
        String url = "http://localhost:8080/your-endpoint";

        // Sender GET request til endpoint
        Response response = RestAssured.get(url);

        // Får responstiden i millisekunder
        long responseTime = response.getTime();

        // Asserter at responstiden er under 2000 ms (2 sekunder)
        assertTrue(responseTime < 2000, "Response time is too high: " + responseTime + "ms");
    }
}

//Denne test sender en enkelt GET-forespørgsel til et endpoint
//og måler responstiden for denne forespørgsel.
//Den asserterer, at responstiden er under 2 sekunder.