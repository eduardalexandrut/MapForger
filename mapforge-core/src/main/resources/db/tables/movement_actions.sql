CREATE TABLE IF NOT EXISTS movement_actions (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    actor INT NOT NULL REFERENCES campaign_actors(id),
    turn_index INT NOT NULL,
    campaign_id INT NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    FOREIGN KEY (turn_index, campaign_id) REFERENCES turns(index, campaign)
);