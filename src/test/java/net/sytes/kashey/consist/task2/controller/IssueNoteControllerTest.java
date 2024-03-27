package net.sytes.kashey.consist.task2.controller;

import net.sytes.kashey.consist.task2.service.IssueNoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(IssueNoteController.class)
class IssueNoteControllerTest {

    @MockBean
    private IssueNoteService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addNote_ReturnsStatus_OK() throws Exception {

        String body = "Test comment";

        when(service.addNote(body)).thenReturn(true);

        mockMvc.perform(post("/api/notes")
                        .param("body", body))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"body\":\"Test comment\"}"));
    }

    @Test
    public void addNote_ReturnsStatus_InternalServerError() throws Exception {

        when(service.addNote("Test comment")).thenReturn(false);

        mockMvc.perform(post("/api/notes")
                        .param("body", "Test comment"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"body\":\"\"}"));
    }
}