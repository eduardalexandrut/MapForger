--liquibase formatted sql

--changeset Eduard:001-create-enums
CREATE TYPE public.alignment_enum AS ENUM (
    'LAWFUL_GOOD', 'NEUTRAL_GOOD', 'CHAOTIC_GOOD',
    'LAWFUL_NEUTRAL', 'TRUE_NEUTRAL', 'CHAOTIC_NEUTRAL',
    'LAWFUL_EVIL', 'NEUTRAL_EVIL', 'CHAOTIC_EVIL'
);

--changeset Eduard:001-create-npc-type-enum
CREATE TYPE public.npc_type AS ENUM (
    'humanoid', 'beast', 'undead', 'fiend', 'celestial',
    'dragon', 'construct', 'fey', 'aberration', 'elemental',
    'plant', 'monstrosity'
);

--changeset Eduard:001-create-race-enum
CREATE TYPE public.race_enum AS ENUM (
    'DWARF', 'ELF', 'HALFLING', 'HUMAN', 'DRAGONBORN',
    'GNOME', 'HALF_ELF', 'HALF_ORC', 'TIEFLING'
);

--changeset Eduard:001-create-users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    pic VARCHAR(255),
    joined_date DATE DEFAULT CURRENT_DATE
);

--changeset Eduard:001-create-maps
CREATE TABLE maps (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    width INTEGER NOT NULL,
    height INTEGER NOT NULL,
    pic VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE
);

--changeset Eduard:001-create-campaigns
CREATE TABLE campaigns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    creator_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    map INTEGER NOT NULL,
    pic VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT campaigns_creator_fkey FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT campaigns_map_fkey FOREIGN KEY (map) REFERENCES maps(id)
);

--changeset Eduard:001-create-characters
CREATE TABLE characters (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    armor INTEGER NOT NULL,
    speed INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL,
    alignment alignment_enum NOT NULL DEFAULT 'LAWFUL_GOOD',
    race race_enum NOT NULL DEFAULT 'HUMAN',
    description TEXT,
    backstory TEXT,
    pic VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT characters_creator_fkey FOREIGN KEY (creator) REFERENCES users(id)
);

--changeset Eduard:001-create-npcs
CREATE TABLE npcs (
    id SERIAL PRIMARY KEY,
    armor INTEGER NOT NULL,
    speed INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL,
    type npc_type NOT NULL
);

--changeset Eduard:001-create-campaign-members
CREATE TABLE campaign_members (
    owner INTEGER NOT NULL,
    campaign UUID NOT NULL,
    role VARCHAR(10) NOT NULL,
    PRIMARY KEY (owner, campaign),
    CONSTRAINT campaign_members_owner_fkey FOREIGN KEY (owner) REFERENCES users(id),
    CONSTRAINT campaign_members_campaign_fkey FOREIGN KEY (campaign) REFERENCES campaigns(id)
);

--changeset Eduard:001-create-campaign-actors
CREATE TABLE campaign_actors (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    xp INTEGER NOT NULL,
    hp INTEGER NOT NULL,
    weapon_damage INTEGER NOT NULL DEFAULT 0,
    owner INTEGER NOT NULL,
    campaign UUID NOT NULL,
    character INTEGER,
    npc INTEGER,
    CONSTRAINT campaign_actors_member_fkey FOREIGN KEY (owner, campaign) REFERENCES campaign_members(owner, campaign),
    CONSTRAINT campaign_actors_character_fkey FOREIGN KEY (character) REFERENCES characters(id),
    CONSTRAINT campaign_actors_npc_fkey FOREIGN KEY (npc) REFERENCES npcs(id)
);