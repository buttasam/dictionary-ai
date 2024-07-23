CREATE TYPE LANGUAGE AS ENUM ('CS', 'EN');

CREATE TABLE dictionary
(
    id                SERIAL PRIMARY KEY,
    word              VARCHAR(255) NOT NULL,
    language          LANGUAGE     NOT NULL,
    created_timestamp DATE         NOT NULL DEFAULT CURRENT_DATE,
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

INSERT INTO dictionary(word, language)
VALUES ('ahoj', 'CS');
INSERT INTO dictionary(word, language)
VALUES ('hi', 'EN');
INSERT INTO dictionary(word, language)
VALUES ('hello', 'EN');

INSERT INTO translation(from_id, to_id)
VALUES (1, 2);
INSERT INTO translation(from_id, to_id)
VALUES (1, 3);

SELECT d_to.word as to_word
FROM dictionary d_from
         JOIN translation t ON t.from_id = d_from.id
         JOIN dictionary d_to ON t.to_id = d_to.id
WHERE d_from.word = 'ahoj';

DROP TABLE dictionary;
DROP TABLE translation;
DROP TYPE LANGUAGE;
