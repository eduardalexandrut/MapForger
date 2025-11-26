DO $$ BEGIN
CREATE TYPE npc_type AS ENUM (
    'humanoid',
    'beast',
    'undead',
    'fiend',
    'celestial',
    'dragon',
    'construct',
    'fey',
    'aberration',
    'elemental',
    'plant',
    'monstrosity'
);
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS npcs (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    armor INT NOT NULL,
    speed INT NOT NULL,
    type race_enum NOT NULL,
    weapon_damage INT NOT NULL
);