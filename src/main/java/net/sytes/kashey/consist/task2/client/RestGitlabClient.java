package net.sytes.kashey.consist.task2.client;

import net.sytes.kashey.consist.task2.config.GitlabProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RestGitlabClient implements GitlabClient {

    private final RestTemplate restTemplate;

    private final GitlabProperties gitlabProperties;

    public RestGitlabClient(RestTemplateBuilder restTemplateBuilder, GitlabProperties gitlabProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.gitlabProperties = gitlabProperties;
    }

    @Override
    public boolean addNote(String body) {
        HttpEntity<Map<String, String>> request = new HttpEntity<>(createRequestBody(body), createHttpHeaders());
        try {
            ResponseEntity<String> responseEntity = restTemplate
                    .postForEntity(getActualUrl(), request, String.class);
            return responseEntity.getStatusCode() == HttpStatus.CREATED;
        } catch (RestClientResponseException e) {
            return false;
        }
    }

    private Map<String, String> createRequestBody(String body) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("body", body);
        return requestBody;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("PRIVATE-TOKEN", gitlabProperties.token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public String getActualUrl() {
        return gitlabProperties.url() + gitlabProperties.project() + "/issues/" + gitlabProperties.issue() + "/notes";
    }
}