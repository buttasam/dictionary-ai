CREATE TYPE LANGUAGE AS ENUM ('CS', 'EN');

CREATE TABLE dictionary
(
    id                SERIAL PRIMARY KEY,
    word              VARCHAR(255) NOT NULL,
    language          LANGUAGE     NOT NULL,
    created_timestamp TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (word, language)
);

CREATE TABLE translation
(
    from_id INT NOT NULL,
    to_id   INT NOT NULL,
    PRIMARY KEY (from_id, to_id),
    FOREIGN KEY (from_id) REFERENCES dictionary (id) ON DELETE CASCADE,
    FOREIGN KEY (to_id) REFERENCES dictionary (id) ON DELETE CASCADE
);
