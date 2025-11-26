CREATE TABLE IF NOT EXISTS attack_actions (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    attacker INT NOT NULL REFERENCES campaign_actors(id),
    attacked INT NOT NULL REFERENCES campaign_actors(id),
    turn_index INT NOT NULL,
    campaign_id INT NOT NULL,
    damage INT NOT NULL,
    FOREIGN KEY (turn_index, campaign_id) REFERENCES turns(index, campaign)
);