package net.sytes.kashey.consist.task2.service;

import net.sytes.kashey.consist.task2.client.GitlabClient;
import net.sytes.kashey.consist.task2.model.Note;
import org.springframework.stereotype.Service;

@Service
public class IssueNoteService {

    private final GitlabClient client;

    public IssueNoteService(GitlabClient client) {
        this.client = client;
    }

    public boolean addNote(String body) {
        return client.addNote(new Note(body));
    }
}
