package net.sytes.kashey.consist.task3.it;

import net.sytes.kashey.consist.task3.repository.ExpressionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
public class CalcRequestIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ExpressionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testAddExpression() {

        webTestClient.post().uri("/api/calcrequest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "expression": "2+2",
                            "needLog": true,
                            "description": ""
                        }
                        """)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testGetResultById() {

        String jsonRequest = """
                {
                    "expression": "2*2",
                    "needLog": true,
                    "status": "IN_PROGRESS",
                    "description": ""
                }
                """;

        webTestClient.post().uri("/api/calcrequest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody()
                .consumeWith(response -> {
                    String location = response.getResponseHeaders().getFirst(HttpHeaders.LOCATION);
                    assertNotNull(location);


                    String[] parts = location.split("/");
                    String id = parts[parts.length - 1];

                    await().atMost(5, TimeUnit.SECONDS)
                            .untilAsserted(() ->
                                    webTestClient.get().uri("/api/calcrequest/{id}", id)
                                            .exchange()
                                            .expectStatus().isOk()
                            );
                });
    }

    @Test
    void testGetAllExpressions() {

        String jsonRequest = """
                {
                    "expression": "2+2",
                    "needLog": true,
                    "status": "IN_PROGRESS",
                    "description": ""
                }
                """;

        webTestClient.post().uri("/api/calcrequest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get().uri("/api/calcrequest")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].status").exists();

    }

    @Test
    void testDeleteById() {

        String id = webTestClient.post().uri("/api/calcrequest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "expression": "2+2",
                            "needLog": true,
                            "description": ""
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .returnResult(String.class)
                .getResponseHeaders().getFirst("Location");

        assert id != null;
        webTestClient.delete().uri(id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateById() {

        String id = webTestClient.post().uri("/api/calcrequest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "expression": "2+2",
                            "needLog": false,
                            "description": ""
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .returnResult(String.class)
                .getResponseHeaders().getFirst("Location");

        assert id != null;
        webTestClient.put().uri(id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "description": "Updated description"
                        }
                        """)
                .exchange()
                .expectStatus().isOk();
    }
}