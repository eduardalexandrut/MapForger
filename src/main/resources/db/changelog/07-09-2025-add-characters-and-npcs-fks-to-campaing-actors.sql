--liquibase formatted sql

--changeset Eduard:9
ALTER TABLE campaign_actors ADD COLUMN character INT;
ALTER TABLE campaign_actors ADD COLUMN npc INT;

ALTER TABLE campaign_actors
ADD CONSTRAINT fk_campaign_actors_characters
FOREIGN KEY (character)
REFERENCES characters(id);

ALTER TABLE campaign_actors
ADD CONSTRAINT fk_campaign_actors_npcs
FOREIGN KEY (npc)
REFERENCES npcs(id);
