create database aggregator;
create table posts
(
    id      serial primary key,
    name    varchar NOT NULL,
    text    text,
    link    varchar      NOT NULL UNIQUE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);