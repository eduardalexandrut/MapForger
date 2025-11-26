CREATE TABLE IF NOT EXISTS campaign_actors (
        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        campaign INT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
        type VARCHAR(3) NOT NULL CHECK (type IN ('PC', 'NPC')),
        ref_id INT NOT NULL, -- points to characters(id) or npcs(id)
        xp INT NOT NULL,
        hp INT NOT NULL
);