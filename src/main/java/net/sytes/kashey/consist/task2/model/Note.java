package net.sytes.kashey.consist.task2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Note(String body, Long id) {

    public Note {
        Objects.requireNonNull(body, "Body must not be null");
    }

    public Note(String body) {
        this(body, null);
    }
}