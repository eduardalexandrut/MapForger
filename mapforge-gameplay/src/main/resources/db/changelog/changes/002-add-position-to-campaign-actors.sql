--liquibase formatted sql

--changeset Eduard:002-add-position-to-campaign-actors
ALTER TABLE campaign_actors ADD COLUMN x INTEGER;
ALTER TABLE campaign_actors ADD COLUMN y INTEGER;