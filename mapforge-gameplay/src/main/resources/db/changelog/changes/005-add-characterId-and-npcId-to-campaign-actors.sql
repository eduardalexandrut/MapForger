--liquibase formatted sql

--changeset Eduard:005-add-characterId-and-npcId-to-campaign-actors.sql
ALTER TABLE campaign_actors ADD COLUMN character_id INT;
ALTER TABLE campaign_actors ADD COLUMN npc_id INT;