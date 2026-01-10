CREATE TABLE ideas
(
    id          VARCHAR(255)                NOT NULL,
    title       VARCHAR(255)                NOT NULL,
    summary     VARCHAR(255)                NOT NULL,
    description VARCHAR(255)                NOT NULL,
    tags        TEXT[]                      NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    author_id   VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_ideas PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE ideas
    ADD CONSTRAINT FK_IDEAS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);