package net.sytes.kashey.consist.task2.client;

import net.sytes.kashey.consist.task2.config.GitlabProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(GitlabClient.class)
class RestGitlabClientTest {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private GitlabClient gitlabClient;

    @MockBean
    private GitlabProperties gitlabProperties;

    @Test
    void addNote_ReturnsStatus_OK() {
        Mockito.when(gitlabProperties.url()).thenReturn("https://gitlab.com/api/v4");
        Mockito.when(gitlabProperties.project()).thenReturn("/projects/12345678");
        Mockito.when(gitlabProperties.issue()).thenReturn("1");

        mockRestServiceServer.expect(ExpectedCount.times(1), requestTo(getActualUrl()))
                .andRespond(withStatus(HttpStatus.CREATED));

        gitlabClient.addNote("Тестовый комментарий");
        this.mockRestServiceServer.verify();
    }

    @Test
    void addNote_NoBody_ReturnsInternalServiceError() throws Exception {

        Mockito.when(gitlabProperties.url()).thenReturn("https://gitlab.com/api/v4");
        Mockito.when(gitlabProperties.project()).thenReturn("/projects/12345678");
        Mockito.when(gitlabProperties.issue()).thenReturn("1");

        this.mockRestServiceServer
                .expect(ExpectedCount.times(1), requestTo(getActualUrl()))
                .andRespond(withServerError());
        Assertions.assertFalse(this.gitlabClient.addNote(null));

        this.mockRestServiceServer.verify();
    }

    private ResponseCreator withServerError() {
        return withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding note")
                .contentType(MediaType.APPLICATION_JSON);
    }

    public String getActualUrl() {
        return "https://gitlab.com/api/v4/projects/12345678/issues/1/notes";
    }
}