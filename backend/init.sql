CREATE TYPE LANGUAGE AS ENUM ('CS', 'EN', 'DE');
CREATE TYPE AUTH_PROVIDER AS ENUM ('LOCAL', 'GOOGLE', 'FACEBOOK');

CREATE TABLE words
(
    id                SERIAL PRIMARY KEY,
    word              VARCHAR(255) NOT NULL,
    language          LANGUAGE     NOT NULL,
    created_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (word, language)
);

CREATE TABLE translations
(
    from_id INT NOT NULL,
    to_id   INT NOT NULL,
    PRIMARY KEY (from_id, to_id),
    FOREIGN KEY (from_id) REFERENCES words (id) ON DELETE CASCADE,
    FOREIGN KEY (to_id) REFERENCES words (id) ON DELETE CASCADE
);

CREATE TABLE users
(
    id                SERIAL PRIMARY KEY,
    username          VARCHAR(500) NOT NULL UNIQUE,
    created_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password_hash     BYTEA        NULL,
    password_salt     BYTEA        NULL,
    access_token      TEXT         NULL
);

CREATE TABLE favorite_words
(
    word_id           INT       NOT NULL,
    user_id           INT       NOT NULL,
    PRIMARY KEY (word_id, user_id),
    created_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (word_id) REFERENCES words (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
