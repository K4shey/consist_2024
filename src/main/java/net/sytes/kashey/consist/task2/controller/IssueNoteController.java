package net.sytes.kashey.consist.task2.controller;

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
    public ResponseEntity<String> addNote(@RequestParam(value = "body", required = false) String body) {

        if (service.addNote(body)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Adding note error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}