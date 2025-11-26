DO $$ BEGIN
CREATE TYPE race_enum AS ENUM (
    'Human', 'Elf', 'Dwarf', 'Halfling', 'Orc', 'Gnome', 'Tiefling', 'Dragonborn'
  );
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
CREATE TYPE alignment_enum AS ENUM (
    'Lawful Good', 'Neutral Good', 'Chaotic Good',
    'Lawful Neutral', 'True Neutral', 'Chaotic Neutral',
    'Lawful Evil', 'Neutral Evil', 'Chaotic Evil'
  );
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;


CREATE TABLE IF NOT EXISTS characters (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    creator INT NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    armor INT NOT NULL,
    speed INT NOT NULL,
    race race_enum NOT NULL,
    alignment alignment_enum NOT NULL,
    weapon_damage INT NOT NULL
);