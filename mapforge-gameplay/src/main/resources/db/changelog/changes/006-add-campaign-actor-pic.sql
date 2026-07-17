--liquibase formatted sql

--changeset Eduard:006-add-campaign-actor-pic.sql
ALTER TABLE campaign_actors ADD COLUMN pic VARCHAR(100) DEFAULT 'npg.png';