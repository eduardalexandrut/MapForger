--liquibase formatted sql
--changeset Eduard:1
ALTER TABLE users ADD COLUMN test INTEGER DEFAULT 0;