--liquibase formatted sql

--changeset Eduard:004-add-created-at-to-attack-actions
ALTER TABLE attack_actions ADD COLUMN created_at TIMESTAMP DEFAULT NOW();

--changeset Eduard:004-add-created-at-to-movement-actions
ALTER TABLE movement_actions ADD COLUMN created_at TIMESTAMP DEFAULT NOW();

--changeset Eduard:004-add-created-at-to-death-actions
ALTER TABLE death_actions ADD COLUMN created_at TIMESTAMP DEFAULT NOW();