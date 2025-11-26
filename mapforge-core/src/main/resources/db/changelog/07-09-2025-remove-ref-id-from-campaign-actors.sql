--liquibase formatted sql

--changeset Eduard:9

ALTER TABLE campaign_actors DROP COLUMN ref_id;