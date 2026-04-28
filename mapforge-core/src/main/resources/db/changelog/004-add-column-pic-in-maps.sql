--liquibase formatted sql
--changeset Eduard:18

ALTER TABLE maps ADD COLUMN pic VARCHAR(512) NOT NULL DEFAULT '../public/dark-fantasy-dungeon.png';