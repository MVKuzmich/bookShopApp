create table books
(
    id            SERIAL       NOT NULL,
    pub_date      DATE         NOT NULL,
    is_bestseller SMALLINT     NOT NULL,
    slug          VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    image         VARCHAR(255),
    description   TEXT,
    price         INT          NOT NULL,
    discount      DECIMAL      NOT NULL DEFAULT 0,
    primary key (id)
);
create table authors
(
    id          SERIAL       NOT NULL,
    photo       VARCHAR(255),
    slug        VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    primary key (id)
);
create table book2author
(
    id         serial NOT NULL,
    book_id    INT    NOT NULL,
    author_id  INT    NOT NULL,
    sort_index INT    NOT NULL DEFAULT 0,
    primary key (id)
);
create table book_review
(
    id      serial    NOT NULL,
    book_id INT       NOT NULL,
    user_id INT       NOT NULL,
    time    timestamp NOT NULL,
    text    TEXT      NOT NULL,
    primary key (id)
);
create table book_review_like
(
    id        serial    NOT NULL,
    review_id INT       NOT NULL,
    user_id   INT       NOT NULL,
    time      timestamp NOT NULL,
    value     SMALLINT  NOT NULL,
    primary key (id)
);
create table book_rates
(
    id      serial not null,
    book_id int    not null,
    value   int    not null,
    primary key (id)
);
create table genres
(
    id        serial       NOT NULL,
    parent_id INT,
    slug      VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    primary key (id)
);
create table tags
(
    id          serial       NOT NULL,
    description VARCHAR(255) NOT NULL,
    primary key (id)
);

create table book2tag
(
    id      serial NOT NULL,
    book_id INT    NOT NULL,
    tag_id  INT    NOT NULL,
    primary key (id)
);

create table book2genre
(
    id       serial NOT NULL,
    book_id  INT    NOT NULL,
    genre_id INT    NOT NULL,
    primary key (id)
);
create table users
(
    id       serial       NOT NULL,
    hash     VARCHAR(255) NOT NULL,
    reg_time timestamp    NOT NULL,
    balance  INT          NOT NULL,
    name     VARCHAR(255),
    primary key (id)
);

create table user_contact
(
    id          serial       NOT NULL,
    user_id     INT          NOT NULL,
    current_contact_type varchar(20),
    approved    smallint     NOT NULL,
    code        VARCHAR(255),
    code_trials INT,
    code_time   timestamp,
    contact     VARCHAR(255) NOT NULL,
    primary key (id)
);
create table book2user
(
    id      serial    NOT NULL,
    time    timestamp NOT NULL,
    type_id INT       NOT NULL,
    book_id INT       NOT NULL,
    user_id INT       NOT NULL,
    primary key (id)
);
create table book2user_type
(
    id   serial       NOT NULL,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    primary key (id)
);
create table balance_transaction
(
    id          serial    NOT NULL,
    user_id     INT       NOT NULL,
    time        timestamp NOT NULL,
    value       INT       NOT NULL DEFAULT 0,
    book_id     INT       NOT NULL,
    description TEXT      NOT NULL,
    primary key (id)
);
create table book_file
(
    id      serial       NOT NULL,
    hash    VARCHAR(255) NOT NULL,
    type_id INT          NOT NULL,
    path    VARCHAR(255) NOT NULL,
    book_id INT          NOT NULL,
    primary key (id)
);
create table book_file_type
(
    id          serial       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    primary key (id)
);
create table file_download
(
    id      serial NOT NULL,
    user_id INT    NOT NULL,
    book_id INT    NOT NULL,
    count   INT    NOT NULL DEFAULT 1,
    primary key (id)
);
create table document
(
    id         serial       NOT NULL,
    sort_index INT          NOT NULL DEFAULT 0,
    slug       VARCHAR(255) NOT NULL,
    title      VARCHAR(255) NOT NULL,
    text       TEXT         NOT NULL,
    primary key (id)
);
create table faq
(
    id         serial       NOT NULL,
    sort_index INT          NOT NULL DEFAULT 0,
    question   VARCHAR(255) NOT NULL,
    answer     TEXT         NOT NULL,
    primary key (id)
);
create table message
(
    id      serial       NOT NULL,
    time    timestamp    NOT NULL,
    user_id INT,
    email   VARCHAR(255),
    name    VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    text    TEXT         NOT NULL,
    primary key (id)
);