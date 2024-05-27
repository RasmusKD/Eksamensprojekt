package com.minkostplan.eksamensprojekt;// Sample code using JUnit and RestAssured for testing response time
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {

    @Test
    public void testResponseTime() {
        Response response = RestAssured.get("http://localhost:8080/your-endpoint");
        long responseTime = response.getTime();
        assertTrue(responseTime < 2000, "Response time is too high: " + responseTime + "ms");
    }
}

// dette er en fejl tror jeg