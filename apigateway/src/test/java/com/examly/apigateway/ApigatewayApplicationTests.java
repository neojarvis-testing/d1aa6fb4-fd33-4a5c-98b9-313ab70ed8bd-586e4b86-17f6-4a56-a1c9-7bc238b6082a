package com.examly.apigateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApigatewayApplicationTests {
    private String usertoken;
    private String admintoken;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // To parse JSON responses

    private HttpHeaders createHeaders() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return headers;
    }

    @Test
    @Order(1)
    void backend_testRegisterAdmin() {
            String requestBody = "{\"userId\": 1,\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\", \"username\": \"admin123\", \"userRole\": \"Admin\", \"mobileNumber\": \"9876543210\"}";
            ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                            new HttpEntity<>(requestBody, createHeaders()), String.class);

            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void backend_testRegisterUser() {
            String requestBody = "{\"userId\": 2,\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\", \"username\": \"user123\", \"userRole\": \"User\", \"mobileNumber\": \"1122334455\"}";
            ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                            new HttpEntity<>(requestBody, createHeaders()), String.class);

            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void backend_testLoginAdmin() throws Exception {
            String requestBody = "{\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\"}";

            ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                            new HttpEntity<>(requestBody, createHeaders()), String.class);

            System.out.println("Login Admin Response Status Code: " + response.getStatusCode());
            System.out.println("Login Admin Response Body: " + response.getBody());

            // Check if response body is null
            Assertions.assertNotNull(response.getBody(), "Response body is null!");

            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String token = responseBody.get("token").asText();
            admintoken = token;

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(token);
    }

    @Test
    @Order(4)
    void backend_testLoginUser() throws Exception {
            String requestBody = "{\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\"}";

            ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                            new HttpEntity<>(requestBody, createHeaders()), String.class);

            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String token = responseBody.get("token").asText();
            usertoken = token;

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(token);
    }

    @Test
    @Order(5)
    void backend_testAdminAddArchitect() throws Exception {
            // Debug: Print token values before assertions
            System.out.println("Admin token before test: " + admintoken);
            System.out.println("User token before test: " + usertoken);

            String requestBody = "{"
                            + "\"name\": \"John Doe Architect\","
                            + "\"expertise\": \"Residential Design\","
                            + "\"location\": \"Metropolis\","
                            + "\"rating\": 4.5,"
                            + "\"portfolioLink\": \"http://johndoe.example.com\","
                            + "\"status\": \"Active\""
                            + "}";

            // Test with Admin token (Expecting 201 Created)
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange("/api/architects", HttpMethod.POST,
                            adminRequest,
                            String.class);

            // Debug: Print the status code and response body
            System.out.println("Admin Response Status Code: " + adminResponse.getStatusCode());
            System.out.println("Admin Response Body: " + adminResponse.getBody());

            // Check the status code
            Assertions.assertEquals(HttpStatus.CREATED, adminResponse.getStatusCode(),
                            "Expected 201 Created but got " + adminResponse.getStatusCode() + ". Response body: "
                                            + adminResponse.getBody());

            // Check if the response body is null
            Assertions.assertNotNull(adminResponse.getBody(),
                            "Response body is null. Status: " + adminResponse.getStatusCode());

            JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

            System.out.println("Admin adding architect - Response JSON: " + responseBody.toString());
            Assertions.assertEquals("John Doe Architect", responseBody.get("name").asText());
            Assertions.assertEquals("Residential Design", responseBody.get("expertise").asText());
            Assertions.assertEquals(4.5, responseBody.get("rating").asDouble());
            Assertions.assertEquals("Active", responseBody.get("status").asText());

            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange("/api/architects", HttpMethod.POST,
                            userRequest,
                            String.class);

            // Check the User response
            Assertions.assertTrue(
                            userResponse.getStatusCode() == HttpStatus.FORBIDDEN,
                            "User should not be allowed to add architect. Status code: "
                                            + userResponse.getStatusCode());
    }

    @Test
    @Order(6)
    void backend_testGetArchitectByIdWithRoleValidation() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            long architectId = 1;
            String url = "/api/architects/" + architectId;

            // Test with Admin token (Expecting 200 OK)
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest,
                            String.class);

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin retrieving architect");
            Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

            // Parse and validate JSON response
            JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

            Assertions.assertEquals(architectId, responseBody.get("architectId").asLong());
            Assertions.assertEquals("John Doe Architect", responseBody.get("name").asText());
            Assertions.assertEquals("Residential Design", responseBody.get("expertise").asText());
            Assertions.assertEquals(4.5, responseBody.get("rating").asDouble());
            Assertions.assertEquals("Metropolis", responseBody.get("location").asText());
            Assertions.assertEquals("Active", responseBody.get("status").asText());

            System.out.println("Admin Response JSON: " + responseBody.toString());

            // Test with User token (Expecting 200 OK)
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest,
                            String.class);

            System.out.println(userResponse.getStatusCode() + " Status code for User retrieving architect");
            Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());

    }

    @Test
    @Order(7)
    void backend_testGetAllArchitects() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            // Test GET with Admin token (Expecting 200 OK)
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange("/api/architects", HttpMethod.GET,
                            adminRequest,
                            String.class);
            JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin getting all architects");
            Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
            Assertions.assertTrue(responseBody.isArray());

            // Test GET with User token (Expecting 200 OK)
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange("/api/architects", HttpMethod.GET,
                            userRequest,
                            String.class);
            JsonNode userResponseBody = objectMapper.readTree(userResponse.getBody());

            System.out.println(userResponse.getStatusCode() + " Status code for User getting all architects");
            Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
            Assertions.assertTrue(userResponseBody.isArray());
    }

    @Test
    @Order(8)
    void backend_testUpdateArchitectWithRoleValidation() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            long architectId = 1;
            String url = "/api/architects/" + architectId;

            String updateRequestBody = "{"
                            + "\"architectId\": " + architectId + ","
                            + "\"name\": \"Updated John Doe Architect\","
                            + "\"expertise\": \"Commercial Design\","
                            + "\"location\": \"New City\","
                            + "\"rating\": 4.8,"
                            + "\"portfolioLink\": \"http://updated.johndoe.example.com\","
                            + "\"status\": \"Inactive\""
                            + "}";

            // Test with Admin token (Expecting 200 OK)
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(updateRequestBody, adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.PUT, adminRequest,
                            String.class);

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin updating architect");
            Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

            JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

            Assertions.assertEquals(architectId, responseBody.get("architectId").asLong());
            Assertions.assertEquals("Updated John Doe Architect", responseBody.get("name").asText());
            Assertions.assertEquals("Commercial Design", responseBody.get("expertise").asText());
            Assertions.assertEquals(4.8, responseBody.get("rating").asDouble());
            Assertions.assertEquals("New City", responseBody.get("location").asText());
            Assertions.assertEquals("Inactive", responseBody.get("status").asText());

            System.out.println("Admin Update Response JSON: " + responseBody.toString());

            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(updateRequestBody, userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.PUT, userRequest,
                            String.class);

            System.out.println(userResponse.getStatusCode() + " Status code for User trying to update architect");

            Assertions.assertTrue(
                            userResponse.getStatusCode() == HttpStatus.FORBIDDEN,
                            "User should not be allowed to update architect. Status code: "
                                            + userResponse.getStatusCode());
    }

    @Test
    @Order(9)
    void backend_testUserCanAddInquiry() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(usertoken, "User token should not be null");
            Assertions.assertNotNull(admintoken, "Admin token should not be null");

            String requestBody = "{"
                            + "\"message\": \"Need consultation for a project\","
                            + "\"status\": \"Pending\","
                            + "\"inquiryDate\": \"2025-03-05T10:00:00\"," // Assuming ArchitectInquiry has similar
                                                                          // fields
                            + "\"priority\": \"High\","
                            + "\"contactDetails\": \"client@example.com\","
                            + "\"user\": {\"userId\": 2}," // Assuming user ID 1 exists
                            + "\"architect\": {\"architectId\": 1}" // Link to architect
                            + "}";

            // Test with User token (Expecting 201 Created)
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

            // Assuming the endpoint is /api/inquiries handled by ArchitectInquiryController
            ResponseEntity<String> userResponse = restTemplate.exchange("/api/inquiries", HttpMethod.POST,
                            userRequest,
                            String.class);
            JsonNode responseBody = objectMapper.readTree(userResponse.getBody());

            System.out.println(userResponse.getStatusCode() + " Status code for User adding inquiry");
            Assertions.assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
            Assertions.assertEquals("Need consultation for a project", responseBody.get("message").asText());
            Assertions.assertEquals("Pending", responseBody.get("status").asText());
            Assertions.assertEquals("High", responseBody.get("priority").asText());
            Assertions.assertEquals("client@example.com", responseBody.get("contactDetails").asText());

            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange("/api/inquiries", HttpMethod.POST,
                            adminRequest, String.class);

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin adding inquiry");
            Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                            "Admin should NOT be allowed to add inquiry");

    }

    @Test
    @Order(10)
    void backend_testRoleBasedAccessForViewingAllInquiries() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            String url = "/api/inquiries"; // Assuming endpoint for ArchitectInquiryController

            // Test with Admin token (Expecting 200 OK)
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest,
                            String.class);
            JsonNode adminResponseBody = objectMapper.readTree(adminResponse.getBody());

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin viewing all inquiries");
            Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
            Assertions.assertTrue(adminResponseBody.isArray());

            System.out.println("Admin View Inquiries Response JSON: " + adminResponseBody.toString());

            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest,
                            String.class);

            System.out.println(userResponse.getStatusCode() + " Status code for User trying to view all inquiries");
            Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode(),
                            "User should NOT be allowed to view all inquiries");
    }

    @Test
    @Order(11)
    void backend_testUserCanViewOwnInquiries() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(usertoken, "User token should not be null");
            Assertions.assertNotNull(admintoken, "Admin token should not be null");

            long userId = 2; // Assuming user ID 1 is the logged-in user
            String url = "/api/inquiries/user/" + userId; // Assuming endpoint for ArchitectInquiryController

            // Test with User token (Expecting 200 OK)
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest,
                            String.class);
            JsonNode userResponseBody = objectMapper.readTree(userResponse.getBody());

            System.out.println(userResponse.getStatusCode() + " Status code for User viewing their own inquiries");
            Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
            Assertions.assertTrue(userResponseBody.isArray());

            System.out.println("User View Own Inquiries Response JSON: " + userResponseBody.toString());

            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest,
                            String.class);

            System.out.println(adminResponse.getStatusCode()
                            + " Status code for Admin trying to view user's inquiries");
            Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                            "Admin should NOT be allowed to view a user's own inquiries");
    }

    @Test
    @Order(12)
    void backend_testAddFeedbackWithRoleValidation() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            String requestBody = "{"
                            + "\"feedbackId\": 1,"
                            + "\"feedbackText\": \"Great architectural design service!\","
                            + "\"date\": \"2025-03-05T12:00:00Z\"," // Assuming Feedback has similar fields
                            + "\"category\": \"Design\","
                            + "\"user\": {\"userId\": 2}," // Assuming user ID 1 exists
                            + "\"architect\": {\"architectId\": 1}" // Link to architect
                            + "}";

            // Test with User token (Expecting 201 Created)
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST,
                            userRequest,
                            String.class);
            JsonNode responseBody = objectMapper.readTree(userResponse.getBody());

            System.out.println(userResponse.getStatusCode() + " Status code for User adding feedback");
            Assertions.assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
            Assertions.assertEquals("Great architectural design service!",
                            responseBody.get("feedbackText").asText());
            Assertions.assertEquals("Design", responseBody.get("category").asText());
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST,
                            adminRequest, String.class);

            System.out.println(adminResponse.getStatusCode() + " Status code for Admin trying to add feedback");
            Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                            "Admin should NOT be allowed to add feedback");
    }

    @Test
    @Order(13)
    void backend_testGetAllFeedbackWithRoleValidation() throws Exception {
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            // Admin should be able to view all feedback
            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET,
                            adminRequest,
                            String.class);
            Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET,
                            userRequest, String.class);
            Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode(),
                            "User should NOT be allowed to view all feedback");
            System.out.println(userResponse.getStatusCode() + " Status code for User trying to view all feedback");
    }

    @Test
    @Order(14)
    void backend_testGetFeedbackByUserIdWithRoleValidation() throws Exception {
            Assertions.assertNotNull(admintoken, "Admin token should not be null");
            Assertions.assertNotNull(usertoken, "User token should not be null");

            String url = "/api/feedback/user/2";

            // User should be able to view their own feedback
            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest,
                            String.class);
            Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());

            HttpHeaders adminHeaders = createHeaders();
            adminHeaders.set("Authorization", "Bearer " + admintoken);
            HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);

            ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest,
                            String.class);
            Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                            "Admin should NOT be allowed to view user's feedback");
            System.out.println(
                            adminResponse.getStatusCode() + " Status code for Admin trying to view user feedback");
    }

    @Test
    @Order(15)
    void backend_testUserCanDeleteFeedback() throws Exception {
            // Ensure tokens are available
            Assertions.assertNotNull(usertoken, "User token should not be null");
            Assertions.assertNotNull(admintoken, "Admin token should not be null");

            // Step 1: Create a feedback entry
            String createFeedbackBody = "{"
                            + "\"feedbackId\": 2,"
                            + "\"feedbackText\": \"Very professional architect!\","
                            + "\"date\": \"2025-03-06T14:00:00Z\"," // Assuming Feedback has similar fields
                            + "\"category\": \"Professionalism\","
                            + "\"user\": {\"userId\": 2}," // Assuming user ID 2 exists
                            + "\"architect\": {\"architectId\": 1}" // Link to architect
                            + "}";

            HttpHeaders userHeaders = createHeaders();
            userHeaders.set("Authorization", "Bearer " + usertoken);
            HttpEntity<String> createFeedbackRequest = new HttpEntity<>(createFeedbackBody, userHeaders);

            ResponseEntity<String> createFeedbackResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST,
                            createFeedbackRequest, String.class);

            System.out.println("Create Feedback Response Status Code: " + createFeedbackResponse.getStatusCode());
            System.out.println("Create Feedback Response Body: " + createFeedbackResponse.getBody());

            Assertions.assertEquals(HttpStatus.CREATED, createFeedbackResponse.getStatusCode(),
                            "Expected 201 Created but got " + createFeedbackResponse.getStatusCode()
                                            + ". Response body: "
                                            + createFeedbackResponse.getBody());
            Assertions.assertNotNull(createFeedbackResponse.getBody(), "Create feedback response body is null");

            JsonNode createFeedbackResponseBody = objectMapper.readTree(createFeedbackResponse.getBody());
            long feedbackId = createFeedbackResponseBody.get("feedbackId").asLong();
            Assertions.assertEquals("Very professional architect!",
                            createFeedbackResponseBody.get("feedbackText").asText());
            Assertions.assertEquals("Professionalism", createFeedbackResponseBody.get("category").asText());

            // Step 2: Delete the feedback
            String deleteUrl = "/api/feedback/" + feedbackId;

            HttpEntity<Void> deleteFeedbackRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> deleteFeedbackResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE,
                            deleteFeedbackRequest, String.class);

            System.out.println("Delete Feedback Response Status Code: " + deleteFeedbackResponse.getStatusCode());
            System.out.println("Delete Feedback Response Body: " + deleteFeedbackResponse.getBody());

            Assertions.assertEquals(HttpStatus.OK, deleteFeedbackResponse.getStatusCode(),
                            "Expected 200 OK but got " + deleteFeedbackResponse.getStatusCode()
                                            + ". Response body: "
                                            + deleteFeedbackResponse.getBody());

            // Step 3: Verify the feedback is deleted
            HttpEntity<Void> getFeedbackRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> getFeedbackResponse = restTemplate.exchange(deleteUrl, HttpMethod.GET,
                            getFeedbackRequest, String.class);

            System.out.println("Get Feedback After Deletion Response Status Code: "
                            + getFeedbackResponse.getStatusCode());
            System.out.println("Get Feedback After Deletion Response Body: " + getFeedbackResponse.getBody());

            Assertions.assertEquals(HttpStatus.NOT_FOUND, getFeedbackResponse.getStatusCode(),
                            "Expected 404 Not Found but got " + getFeedbackResponse.getStatusCode()
                                            + ". Response body: "
                                            + getFeedbackResponse.getBody());
    }



     }
