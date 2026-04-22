--liquibase formatted sql

--changeset eduard:001-create-campaign-actors
CREATE TABLE campaign_actors (
    id INTEGER PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    hp INTEGER NOT NULL,
    xp INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL,
    owner_id INTEGER NOT NULL,
    campaign_id UUID NOT NULL
);

--changeset eduard:001-create-game-sessions
CREATE TABLE game_sessions (
    campaign_id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    current_turn_index INTEGER NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP
);

--changeset eduard:001-create-game-session-turn-order
CREATE TABLE game_session_turn_order (
    campaign_id UUID NOT NULL,
    actor_id INTEGER NOT NULL,
    position INTEGER NOT NULL,
    CONSTRAINT fk_turn_order_session FOREIGN KEY (campaign_id) REFERENCES game_sessions(campaign_id)
);

--changeset eduard:001-create-turns
CREATE TABLE turns (
    index INTEGER NOT NULL,
    campaign_id UUID NOT NULL,
    actor_id INTEGER NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_turns PRIMARY KEY (index, campaign_id)
);

--changeset eduard:001-create-attack-actions
CREATE TABLE attack_actions (
    id SERIAL PRIMARY KEY,
    attacker_id INTEGER NOT NULL,
    attacked_id INTEGER NOT NULL,
    turn_index INTEGER NOT NULL,
    campaign_id UUID NOT NULL,
    damage INTEGER NOT NULL
);

--changeset eduard:001-create-movement-actions
CREATE TABLE movement_actions (
    id SERIAL PRIMARY KEY,
    actor_id INTEGER NOT NULL,
    turn_index INTEGER NOT NULL,
    campaign_id UUID NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL
);

--changeset eduard:001-create-death-actions
CREATE TABLE death_actions (
    id SERIAL PRIMARY KEY,
    actor_id INTEGER NOT NULL,
    killer_id INTEGER NOT NULL,
    turn_index INTEGER NOT NULL,
    campaign_id UUID NOT NULL
);