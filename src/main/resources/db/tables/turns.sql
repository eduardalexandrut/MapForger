CREATE TABLE IF NOT EXISTS turns (
    index INT NOT NULL,
    actor INT NOT NULL REFERENCES campaign_actors(id),
    campaign INT NOT NULL REFERENCES campaigns(id),
    PRIMARY KEY (index, campaign)
);