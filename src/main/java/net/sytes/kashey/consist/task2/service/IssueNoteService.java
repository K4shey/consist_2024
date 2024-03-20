package net.sytes.kashey.consist.task2.service;

import net.sytes.kashey.consist.task2.client.GitlabClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class IssueNoteService {

    private final GitlabClient client;

    public IssueNoteService(GitlabClient client) {
        this.client = client;
    }

    public boolean addNote(String body) {
        try {
            return client.addNote(body);
        } catch (RestClientResponseException e) {
            return false;
        }
    }
}
