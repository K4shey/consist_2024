package net.sytes.kashey.consist.task3.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CalcRequestIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testAddExpression() {

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testGetResultById() {

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
                .exchange();

        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        webTestClient.get().uri("/api/calcrequest/1")
                                .exchange()
                                .expectStatus().isAccepted()
                );

        webTestClient.delete().uri("/api/calcrequest/1");

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

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=true")
                .exchange();

        webTestClient.delete().uri("/api/calcrequest/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateById() {

        webTestClient.post().uri("/api/calcrequest?expr=2+2&needlog=false")
                .exchange();

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        webTestClient.put().uri("/api/calcrequest/1?expr=3+2&needlog=false")
                                .contentType(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isOk()
                );
    }
}