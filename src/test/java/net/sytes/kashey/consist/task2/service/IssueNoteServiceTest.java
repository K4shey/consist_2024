package net.sytes.kashey.consist.task2.service;


import net.sytes.kashey.consist.task2.client.GitlabClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IssueNoteServiceTest {

    @Autowired
    private IssueNoteService issueNoteService;

    @MockBean
    private GitlabClient gitlabClient;

    @Test
    void addNote_WithBody_ReturnsTrue() {

        Mockito.when(gitlabClient.addNote("Тестовый комментарий")).thenReturn(true);

        boolean result = issueNoteService.addNote("Тестовый комментарий");

        assertTrue(result);
    }

    @Test
    void addNote_WithoutBody_ReturnsFalse() {

        Mockito.when(gitlabClient.addNote("Тестовый комментарий")).thenReturn(true);

        boolean result = issueNoteService.addNote(null);

        assertFalse(result);
    }
}