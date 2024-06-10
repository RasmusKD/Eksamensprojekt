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
    //annotationen indikere at det er en test
    @Test
    public void testAverageLoadingTime() {
        // URL til dit endpoint, juster dette til din faktiske URL
        String url = "http://localhost:8080/weekly-recipes";
                                    //initialisere variablen numberOfRequest til værdien 10
        int numberOfRequests = 10; // Antal forespørgsler for at beregne gennemsnit
        long totalResponseTime = 0;//variablen til at samle den samlede responstid for forespørgslerne

        //Loop for at Sende Forespørgsler og Beregne Responstid:
        //initialesere variablen i til 0
        //condition er kontrollering om i er mindre en numberofrequests
        //efter hver iteration af løkken øges værdien
        for (int i = 0; i < numberOfRequests; i++) {
            Response response = RestAssured.get(url); //sender HTTP Get-anmoding til den url der er angivet
            totalResponseTime += response.getTime(); //Responstiden for hver forespørgsel tilføjes til totalResponseTime.
        }

        //Den gennemsnitlige responstid beregnes ved at dividere totalResponseTime med numberOfRequests.
        long averageResponseTime = totalResponseTime / numberOfRequests;

        System.out.println("Gennemsnitlig indlæsningstid: " + averageResponseTime + "ms");

        // Asserter at gennemsnitlig indlæsningstid er under 3000 ms (3 sekunder)
        assertTrue(averageResponseTime < 3000, "Average response time is too high: " + averageResponseTime + "ms");
    } //assertTure kontrollere om en given betingelse er sand
}
