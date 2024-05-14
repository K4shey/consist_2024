CREATE TABLE expressions
(
    id          SERIAL
        PRIMARY KEY,
    description VARCHAR(255),
    expression  VARCHAR(255),
    log         BOOLEAN,
    result      DOUBLE PRECISION NOT NULL,
    status      VARCHAR(255)
        CONSTRAINT expressions_status_check
            CHECK ((status)::TEXT = ANY
                   ((ARRAY ['IN_PROGRESS'::CHARACTER VARYING, 'COMPLETED'::CHARACTER VARYING])::TEXT[]))
);

ALTER TABLE expressions
    OWNER TO "user";