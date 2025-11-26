--liquibase formatted sql

--changeset Eduard:1756237235394-1
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    username VARCHAR(255) DEFAULT 'username' NOT NULL
);

--changeset Eduard:1756237235394-2
CREATE TABLE campaign_actors (
    id SERIAL PRIMARY KEY,
    campaign INTEGER NOT NULL,
        xp INTEGER NOT NULL,
        hp INTEGER NOT NULL,
        type VARCHAR(3) NOT NULL,
        ref_id INTEGER NOT NULL
);

--changeset Eduard:1756237235394-3
CREATE TABLE campaign_members (
    owner INTEGER NOT NULL,
    campaign INTEGER NOT NULL,
    role VARCHAR(10) NOT NULL,
    PRIMARY KEY (owner, campaign)
);

--changeset Eduard:1756237235394-4
CREATE TYPE public.alignment_enum AS ENUM
    ('LAWFUL_GOOD', 'NEUTRAL_GOOD', 'CHAOTIC_GOOD', 'LAWFUL_NEUTRAL', 'TRUE_NEUTRAL', 'CHAOTIC_NEUTRAL', 'LAWFUL_EVIL', 'NEUTRAL_EVIL', 'CHAOTIC_EVIL');

--changeset Eduard:1756237235394-5
CREATE TYPE public.npc_type AS ENUM
    ('humanoid', 'beast', 'undead', 'fiend', 'celestial', 'dragon', 'construct', 'fey', 'aberration', 'elemental', 'plant', 'monstrosity')

--changeset Eduard:1756237235394-6
CREATE TYPE public.race_enum AS ENUM
    ('DWARF', 'ELF', 'HALFLING', 'HUMAN', 'DRAGONBORN', 'GNOME', 'HALF_ELF', 'HALF_ORC', 'TIEFLING');

--changeset Eduard:1756237235394-7
CREATE TABLE characters (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    armor INTEGER NOT NULL,
    speed INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL,
    alignment ALIGNMENT_ENUM DEFAULT 'LAWFUL_GOOD' NOT NULL,
    race RACE_ENUM DEFAULT 'HUMAN' NOT NULL
);

--changeset Eduard:1756237235394-8
CREATE TABLE npcs (
    id SERIAL PRIMARY KEY,
    armor INTEGER NOT NULL,
    speed INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL,
    race RACE_ENUM DEFAULT 'HUMAN' NOT NULL
);

--changeset Eduard:1756237235394-9
CREATE TABLE attack_actions (
    id SERIAL PRIMARY KEY,
    attacker INTEGER NOT NULL,
    attacked INTEGER NOT NULL,
    turn_index INTEGER NOT NULL,
    campaign_id INTEGER NOT NULL,
    damage INTEGER NOT NULL
);

--changeset Eduard:1756237235394-10
CREATE TABLE campaigns (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    map INTEGER NOT NULL
);

--changeset Eduard:1756237235394-11
CREATE TABLE maps (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    width INTEGER NOT NULL,
    height INTEGER NOT NULL
);

--changeset Eduard:1756237235394-12
CREATE TABLE movement_actions (
    id SERIAL PRIMARY KEY,
    actor INTEGER NOT NULL,
    turn_index INTEGER NOT NULL,
    campaign_id INTEGER NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL
);

--changeset Eduard:1756237235394-13
CREATE TABLE turns (
    index INTEGER NOT NULL,
    actor INTEGER NOT NULL,
    campaign INTEGER NOT NULL,
    PRIMARY KEY (index, campaign)
);

--changeset Eduard:1756237235394-14
ALTER TABLE attack_actions
ADD CONSTRAINT attack_actions_attacked_fkey
FOREIGN KEY (attacked) REFERENCES campaign_actors(id);

--changeset Eduard:1756237235394-15
ALTER TABLE attack_actions
ADD CONSTRAINT attack_actions_attacker_fkey
FOREIGN KEY (attacker) REFERENCES campaign_actors(id);

--changeset Eduard:1756237235394-16
ALTER TABLE attack_actions
ADD CONSTRAINT attack_actions_turn_index_campaign_id_fkey
FOREIGN KEY (turn_index, campaign_id) REFERENCES turns(index, campaign);

--changeset Eduard:1756237235394-17
ALTER TABLE campaign_actors
ADD CONSTRAINT campaign_actors_campaign_fkey
FOREIGN KEY (campaign) REFERENCES campaigns(id);

--changeset Eduard:1756237235394-18
ALTER TABLE campaign_members
ADD CONSTRAINT campaign_members_campaign_fkey
FOREIGN KEY (campaign) REFERENCES campaigns(id);

--changeset Eduard:1756237235394-19
ALTER TABLE campaign_members
ADD CONSTRAINT campaign_members_owner_fkey
FOREIGN KEY (owner) REFERENCES users(id);

--changeset Eduard:1756237235394-20
ALTER TABLE campaigns
ADD CONSTRAINT campaigns_map_fkey
FOREIGN KEY (map) REFERENCES maps(id);

--changeset Eduard:1756237235394-21
ALTER TABLE characters
ADD CONSTRAINT characters_creator_fkey
FOREIGN KEY (creator) REFERENCES users(id);

--changeset Eduard:1756237235394-22
ALTER TABLE movement_actions
ADD CONSTRAINT movement_actions_actor_fkey
FOREIGN KEY (actor) REFERENCES campaign_actors(id);

--changeset Eduard:1756237235394-23
ALTER TABLE movement_actions
ADD CONSTRAINT movement_actions_turn_index_campaign_id_fkey
FOREIGN KEY (turn_index, campaign_id) REFERENCES turns(index, campaign);

--changeset Eduard:1756237235394-24
ALTER TABLE turns
ADD CONSTRAINT turns_actor_fkey
FOREIGN KEY (actor) REFERENCES campaign_actors(id);

--changeset Eduard:1756237235394-25
ALTER TABLE turns
ADD CONSTRAINT turns_campaign_fkey
FOREIGN KEY (campaign) REFERENCES campaigns(id);


--include file:src/main/resources/db/changelog/26-08-2025-add-test-column-to-users.sql
--include file:src/main/resources/db/changelog/26-08-2025-remove-test-column-from-users.sql
--include file:src/main/resources/db/changelog/30-08-2025-change-campaigns-pk-to-string.sql
--include file:src/main/resources/db/changelog/31-08-2025-add-foreignkey-campaign_actor-to-campaign_member.sql
--include file:src/main/resources/db/changelog/07-09-2025-remove-ref-id-from-campaign-actors.sql
--include file:src/main/resources/db/changelog/07-09-2025-add-characters-and-npcs-fks-to-campaing-actors.sql
