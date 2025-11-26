CREATE TABLE IF NOT EXISTS campaign_members (
    owner INT NOT NULL REFERENCES users(id),
    campaign UUID NOT NULL REFERENCES campaigns(id),
    role VARCHAR(10) NOT NULL CHECK(role in ('Master', 'Player')),
    PRIMARY KEY (owner, campaign)
);