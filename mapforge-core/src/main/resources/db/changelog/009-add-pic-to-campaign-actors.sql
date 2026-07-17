--liquibase formatted sql

--changeset Eduard:009-add-pic-to-campaign-actors
ALTER TABLE campaign_actors ADD COLUMN pic VARCHAR(255) NOT NULL DEFAULT 'npc.png';