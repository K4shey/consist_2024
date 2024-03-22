package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.GitlabClient;
import net.sytes.kashey.consist.task2.config.GitlabProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest
//@RestClientTest(GitlabClient.class)
class IssueNoteServiceTest {
    MockRestServiceServer mockRestServiceServer;

    @Autowired
    private GitlabClient gitlabClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GitlabProperties gitlabProperties;

    @BeforeEach
    public void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void addNote_withBody_ReturnsSuccess() throws Exception {
        mockRestServiceServer
                .expect(requestTo(getActualUrl()))
                .andRespond(withSuccess());
        gitlabClient.addNote("Тестовый комментарий");
        mockRestServiceServer.verify();
    }

    @Test
    void addNote_NoBody_ReturnsInternalServiceError() throws Exception {
        mockRestServiceServer
                .expect(requestTo(getActualUrl()))
                .andRespond(withServerError());
        gitlabClient.addNote(null);
        mockRestServiceServer.verify();
    }

    private ResponseCreator withServerError() {
        return withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding note")
                .contentType(MediaType.APPLICATION_JSON);
    }

    public String getActualUrl() {
        return gitlabProperties.url() + gitlabProperties.project() + "/issues/" + gitlabProperties.issue() + "/notes";
    }
}