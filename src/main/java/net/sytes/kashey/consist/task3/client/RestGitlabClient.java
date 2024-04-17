package net.sytes.kashey.consist.task3.client;

import net.sytes.kashey.consist.task3.config.GitlabProperties;
import net.sytes.kashey.consist.task3.dto.IntegrationDto;
import net.sytes.kashey.consist.task3.mapper.NoteMapper;
import net.sytes.kashey.consist.task3.model.Note;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class RestGitlabClient implements GitlabClient {
    private final RestTemplate restTemplate;

    private final GitlabProperties gitlabProperties;

    public RestGitlabClient(RestTemplateBuilder restTemplateBuilder, GitlabProperties gitlabProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.gitlabProperties = gitlabProperties;
    }

    @Override
    public boolean addNote(Note note) {
        if (note == null) {
            return false;
        }
        HttpEntity<Map<String, String>> request = new HttpEntity<>(createRequestBody(NoteMapper.INSTANCE.ToDto(note)),
                createHttpHeaders());
        try {

            ResponseEntity<String> responseEntity = restTemplate
                    .postForEntity(UriComponentsBuilder.newInstance()
                            .scheme("https")
                            .host("gitlab.com")
                            .path("/api/v4/projects/{project_id}/issues/{issue_id}/notes")
                            .buildAndExpand(gitlabProperties.project(), gitlabProperties.issue())
                            .toUriString(), request, String.class);
            return responseEntity.getStatusCode() == HttpStatus.CREATED;
        } catch (RestClientResponseException e) {
            return false;
        }
    }

    private Map<String, String> createRequestBody(IntegrationDto dto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("body", dto.body());
        return requestBody;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("PRIVATE-TOKEN", gitlabProperties.token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}