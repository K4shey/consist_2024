package net.sytes.kashey.consist.task2.controller;

import net.sytes.kashey.consist.task2.dto.NoteDto;
import net.sytes.kashey.consist.task2.service.IssueNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notes")
public class IssueNoteController {

    private final IssueNoteService service;

    public IssueNoteController(IssueNoteService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<NoteDto> addNote(@RequestParam(value = "body") String body) {

        if (service.addNote(body)) {
            return new ResponseEntity<>(new NoteDto(body), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new NoteDto(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}