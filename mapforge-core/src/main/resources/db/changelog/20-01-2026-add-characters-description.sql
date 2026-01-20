--liquibase formatted sql

--changeset Eduard:11

ALTER TABLE characters ADD COLUMN description TEXT;