--liquibase formatted sql

--changeset Eduard:8
ALTER TABLE campaign_actors ADD COLUMN owner INTEGER NOT NULL;

ALTER TABLE campaign_actors
ADD CONSTRAINT fk_campaign_actors_campaign_members
FOREIGN KEY (owner, campaign)
REFERENCES campaign_members(owner, campaign)
ON DELETE CASCADE;
