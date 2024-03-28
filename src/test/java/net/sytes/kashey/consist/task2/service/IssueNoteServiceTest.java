package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.RestGitlabClient;
import net.sytes.kashey.consist.task2.model.Note;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueNoteServiceTest {
    @Mock
    private RestGitlabClient gitlabClient;

    private IssueNoteService issueNoteService;

    private Note note;

    @BeforeEach
    public void Initialization() {
        note = new Note("Тестовый комментарий");
        issueNoteService = new IssueNoteService(gitlabClient);
    }

    @Test
    void addNote_WithBody_ReturnsTrue() {

        when(gitlabClient.addNote(note)).thenReturn(true);
        assertThat(issueNoteService.addNote(note.body())).isTrue();
    }

    @Test
    void addNote_WithoutBody_ReturnsFalse() {

        Assertions.assertThrows(NullPointerException.class, () -> issueNoteService.addNote(null));
    }
}