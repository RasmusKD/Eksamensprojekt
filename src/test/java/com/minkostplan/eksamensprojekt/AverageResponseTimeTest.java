package com.minkostplan.eksamensprojekt;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse for at måle gennemsnitlig responstid for et endpoint.
 */
public class AverageResponseTimeTest {

    /**
     * Test for at måle gennemsnitlig indlæsningstid for et endpoint.
     */
    @Test
    public void testAverageLoadingTime() {
        // URL til dit endpoint, juster dette til din faktiske URL
        String url = "http://localhost:8080/your-endpoint";

        int numberOfRequests = 10; // Antal forespørgsler for at beregne gennemsnit
        long totalResponseTime = 0;

        for (int i = 0; i < numberOfRequests; i++) {
            Response response = RestAssured.get(url);
            totalResponseTime += response.getTime();
        }

        long averageResponseTime = totalResponseTime / numberOfRequests;

        System.out.println("Gennemsnitlig indlæsningstid: " + averageResponseTime + "ms");

        // Asserter at gennemsnitlig indlæsningstid er under 3000 ms (3 sekunder)
        assertTrue(averageResponseTime < 3000, "Average response time is too high: " + averageResponseTime + "ms");
    }
}
