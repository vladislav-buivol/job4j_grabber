create database aggregator;
create table post
(
    id      serial primary key,
    name    varchar(100) NOT NULL,
    text    text,
    link    varchar      NOT NULL UNIQUE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);