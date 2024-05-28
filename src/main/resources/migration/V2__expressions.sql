ALTER TABLE expressions -- таблица содержит информацию по выражениям
    ADD COLUMN deleted BOOLEAN default FALSE; -- признак пометки 'удалено', по умолчанию FALSE

ALTER TABLE expressions
    OWNER TO "user";