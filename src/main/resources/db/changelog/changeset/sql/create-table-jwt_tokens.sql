create table jwt_tokens
(
    id              serial NOT NULL,
    token_value     text   NOT NULL,
    expiration_time date   not null,
    primary key (id)
);