package net.sytes.kashey.consist.task2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Note {
    private final String body;
    private Long id;

    public Note(String body) {
        this.body = body;
    }

    public Note(String body, Long id) {
        this.body = body;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "body='" + body + '\'' +
                ", id=" + id +
                '}';
    }
}
