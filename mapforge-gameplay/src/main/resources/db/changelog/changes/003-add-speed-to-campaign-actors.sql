--liquibase formatted sql

--changeset Eduard:003-add-speed-to-campaign-actors
ALTER TABLE campaign_actors ADD COLUMN speed INTEGER NOT NULL DEFAULT 0;