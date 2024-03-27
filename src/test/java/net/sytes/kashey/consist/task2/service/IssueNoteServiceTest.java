package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.GitlabClient;
import net.sytes.kashey.consist.task2.config.GitlabProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GitlabClient.class)
class IssueNoteServiceTest {

    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Autowired
    private GitlabClient gitlabClient;

    @MockBean
    private GitlabProperties gitlabProperties;

    @Test
    void addNote_withBody_ReturnsSuccess() throws Exception {

        Mockito.when(gitlabProperties.url()).thenReturn("https://gitlab.com/api/v4");
        Mockito.when(gitlabProperties.project()).thenReturn("/projects/55606763");
        Mockito.when(gitlabProperties.issue()).thenReturn("/2");

        mockRestServiceServer
                .expect(requestTo(getActualUrl()))
                .andRespond(withSuccess());

        gitlabClient.addNote("Тестовый комментарий");
        mockRestServiceServer.verify();
    }

    @Test
    void addNote_NoBody_ReturnsInternalServiceError() throws Exception {

        Mockito.when(gitlabProperties.url()).thenReturn("https://gitlab.com/api/v4");
        Mockito.when(gitlabProperties.project()).thenReturn("/projects/55606763");
        Mockito.when(gitlabProperties.issue()).thenReturn("/2");

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
        return "https://gitlab.com/api/v4/projects/55606763/issues/2/notes";
    }
}