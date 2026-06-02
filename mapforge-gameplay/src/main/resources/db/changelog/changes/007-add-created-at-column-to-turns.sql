--liquibase formatted sql

--changeset Eduard:007-add-created-at-column-to-turns.sql
ALTER TABLE turns ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();