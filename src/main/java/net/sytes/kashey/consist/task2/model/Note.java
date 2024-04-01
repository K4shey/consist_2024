package net.sytes.kashey.consist.task2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Note(String body,
                   Long id,
                   Boolean system,
                   LocalDate updatedAt,
                   Boolean resolved) {

    public Note {
        Objects.requireNonNull(body, "Body must not be null");
    }

    public Note(String body) {
        this(body, UUID.randomUUID().getMostSignificantBits(), false, LocalDate.now(), false);
    }
}