--liquibase formatted sql

--changeset Eduard:12

ALTER TABLE characters ADD COLUMN pic BYTEA;
ALTER TABLE users ADD COLUMN pic BYTEA;