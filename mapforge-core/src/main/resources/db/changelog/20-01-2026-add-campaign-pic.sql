--liquibase formatted sql

--changeset Eduard:13

ALTER TABLE campaigns ADD COLUMN pic BYTEA;
