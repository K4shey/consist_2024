package net.sytes.kashey.consist.task3.it;

import net.sytes.kashey.consist.task3.repository.ExpressionRepository;
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

    @Test
    void testAddExpression() {

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testGetResultById() {

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
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

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
                .exchange();

        webTestClient.get().uri("/api/calcrequest")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].status").exists();
    }

    @Test
    void testDeleteById() {

        String id = webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
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

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=false")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody()
                .consumeWith(response -> {
                    String location = response.getResponseHeaders().getFirst(HttpHeaders.LOCATION);
                    assertNotNull(location);

                    String[] parts = location.split("/");
                    String id = parts[parts.length - 1];

                    webTestClient.put().uri("/api/calcrequest/{id}?expr=3+2&needlog=false", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isOk();
                });
    }
}