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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(body, note.body) && Objects.equals(system, note.system)
                && Objects.equals(updatedAt, note.updatedAt) && Objects.equals(resolved, note.resolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, system, updatedAt, resolved);
    }
}