--liquibase formatted sql

--changeset Eduard:10
ALTER TABLE characters ADD COLUMN backstory TEXT;