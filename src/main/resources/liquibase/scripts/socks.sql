-- liquibase formatted sql

-- changeset albert:1

create table socks
(
    id              BIGSERIAL PRIMARY KEY,
    color           VARCHAR (100),
    cotton_part     INTEGER,
    quantity        INTEGER
);

INSERT INTO socks(color, cotton_part, quantity) VALUES
                                                    ('black', 10, 10),
                                                    ('red', 20, 10),
                                                    ('yellow', 30, 10),
                                                    ('white', 40, 10),
                                                    ('blue', 50, 10),
                                                    ('black', 60, 20),
                                                    ('red', 70, 20),
                                                    ('yellow', 80, 20),
                                                    ('white', 90, 20),
                                                    ('blue', 100, 20);
