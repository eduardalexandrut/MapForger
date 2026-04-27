--liquibase formatted sql

--changeset Eduard:27-04-2026-drop-attack-actions-fkeys
ALTER TABLE attack_actions DROP CONSTRAINT IF EXISTS attack_actions_attacked_fkey;
ALTER TABLE attack_actions DROP CONSTRAINT IF EXISTS attack_actions_attacker_fkey;
ALTER TABLE attack_actions DROP CONSTRAINT IF EXISTS attack_actions_turn_index_campaign_id_fkey;

--changeset Eduard:27-04-2026-drop-movement-actions-fkeys
ALTER TABLE movement_actions DROP CONSTRAINT IF EXISTS movement_actions_actor_fkey;
ALTER TABLE movement_actions DROP CONSTRAINT IF EXISTS movement_actions_turn_index_campaign_id_fkey;

--changeset Eduard:27-04-2026-drop-turns-fkeys
ALTER TABLE turns DROP CONSTRAINT IF EXISTS turns_actor_fkey;
ALTER TABLE turns DROP CONSTRAINT IF EXISTS turns_campaign_fkey;

--changeset Eduard:27-04-2026-drop-gameplay-tables
DROP TABLE IF EXISTS attack_actions;
DROP TABLE IF EXISTS movement_actions;
DROP TABLE IF EXISTS turns;

--changeset Eduard:27-04-2026-add-weapon-damage-to-campaign-actors
ALTER TABLE campaign_actors ADD COLUMN IF NOT EXISTS weapon_damage INTEGER NOT NULL DEFAULT 0;