package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.RestGitlabClient;
import net.sytes.kashey.consist.task2.model.Note;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueNoteServiceTest {
    @Mock
    private RestGitlabClient gitlabClient;

    @InjectMocks
    private IssueNoteService issueNoteService;

    Note note;

    @BeforeEach
    public void Initialization() {
        note = new Note("Тестовый комментарий");
    }

    @Test
    void addNote_WithBody_ReturnsTrue() {
        when(gitlabClient.addNote(note)).thenReturn(true);
        assertTrue(issueNoteService.addNote(note.body()));
    }

    @Test
    void addNote_WithoutBody_ReturnsFalse() {

        Assertions.assertThrows(NullPointerException.class, () -> issueNoteService.addNote(null));
    }
}