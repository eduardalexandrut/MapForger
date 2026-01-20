--liquibase sql

--changeset Eduard:14

ALTER TABLE users ADD COLUMN joined_date DATE DEFAULT CURRENT_DATE;