package net.sytes.kashey.consist.task2.client;

import net.sytes.kashey.consist.task2.util.ClientUtil;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class RestGitlabClient implements GitlabClient {

    private final Environment environment;

    private final RestTemplate restTemplate;

    public RestGitlabClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Override
    public boolean addNote(String body) {
        String token = environment.getProperty("gitlab.token");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("PRIVATE-TOKEN", token);
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("body", body);
        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(ClientUtil.getActualUrl(environment), request, String.class);
        return responseEntity.getStatusCode() == HttpStatus.CREATED;
    }
}