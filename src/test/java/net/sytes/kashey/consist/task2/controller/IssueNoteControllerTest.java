package net.sytes.kashey.consist.task2.controller;

import net.sytes.kashey.consist.task2.service.IssueNoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class IssueNoteControllerTest {

    @Mock
    private IssueNoteService service;

    @InjectMocks
    private IssueNoteController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void addNote_ReturnsStatus_OK() throws Exception {

        String body = "Тестовый комментарий";

        when(service.addNote(body)).thenReturn(true);

        mockMvc.perform(post("/api/notes")
                        .param("body", body))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    public void addNote_ReturnsStatus_InternalServerError() throws Exception {

        when(service.addNote("Тестовый комментарий")).thenReturn(false);

        mockMvc.perform(post("/api/notes")
                        .param("body", "Тестовый комментарий"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Adding note error"));
    }
}