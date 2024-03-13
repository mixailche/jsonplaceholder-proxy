CREATE TABLE users
(
    id           BIGSERIAL,
    login        VARCHAR(255) NOT NULL,
    password_sha VARCHAR      NOT NULL,

    PRIMARY KEY (id),
    UNIQUE      (login)
);

CREATE TYPE access_level AS ENUM ('NOTHING', 'VIEW', 'EDIT');

CREATE TABLE roles
(
    id                  SERIAL,
    name                VARCHAR(255) NOT NULL UNIQUE,
    posts_access_level  access_level NOT NULL,
    users_access_level  access_level NOT NULL,
    albums_access_level access_level NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id INT    NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TYPE event_method AS ENUM ('GET', 'POST', 'PUT', 'DELETE');
CREATE TYPE event_result AS ENUM ('SATISFIED', 'BAD_REQUEST', 'ACCESS_DENIED');

CREATE TABLE events
(
    id              BIGSERIAL,
    user_id         BIGINT       NOT NULL,
    creation_time   TIMESTAMP    NOT NULL DEFAULT (TIMEZONE('utc', NOW())),
    query_url       VARCHAR(255) NOT NULL,
    required_access access_level NOT NULL,
    method          event_method NOT NULL,
    result          event_result NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
