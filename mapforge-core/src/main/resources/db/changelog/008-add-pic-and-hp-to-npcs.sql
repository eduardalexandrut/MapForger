--liquibase formatted sql

--changeset Eduard:008-add-pic-and-hp-to-npcs
ALTER TABLE npcs ADD COLUMN pic VARCHAR(255) NOT NULL DEFAULT 'npc.png';

ALTER TABLE npcs ADD COLUMN hp INT NOT NULL DEFAULT 10;