CREATE TYPE expressionstatus AS ENUM (
    'IN_PROGRESS',
    'COMPLETED'); -- статус выражения (в процессе вычисления, вычислено)

CREATE TABLE expressions -- таблица содержит информацию по выражениям
(
    id          SERIAL PRIMARY KEY, -- идентификатор выражения
    description VARCHAR(255), -- произвольное описание
    expression  VARCHAR(255) NOT NULL, -- текст самого выражения
    log         BOOLEAN, -- признак того, что требуется логирование
    result      DOUBLE PRECISION NOT NULL, -- результат вычисления
    status      EXPRESSIONSTATUS DEFAULT 'IN_PROGRESS' -- статус выражения по-умолчанию: в процессе вычисления
);

ALTER TABLE expressions
    OWNER TO "user";