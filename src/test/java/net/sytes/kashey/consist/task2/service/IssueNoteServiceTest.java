package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.GitlabClient;
import net.sytes.kashey.consist.task2.util.ClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockRestServiceServer
class IssueNoteServiceTest {

    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Autowired
    GitlabClient gitlabClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @BeforeEach
    public void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void addNote_withBody_ReturnsSuccess() throws Exception {
        mockRestServiceServer
                .expect(requestTo(ClientUtil.getActualUrl(environment)))
                .andRespond(withSuccess());
        gitlabClient.addNote("Тестовый комментарий");
        mockRestServiceServer.verify();
    }

    @Test
    void addNote_throwsException() throws Exception {
        mockRestServiceServer
                .expect(requestTo(ClientUtil.getActualUrl(environment)))
                .andRespond(withServerError());
        assertThrows(RestClientResponseException.class, () -> {
            gitlabClient.addNote(null);
        });
        mockRestServiceServer.verify();
    }

    private ResponseCreator withServerError() {
        return withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding note")
                .contentType(MediaType.APPLICATION_JSON);
    }
}