--liquibase formatted sql

--changeset Eduard:006-create-session-presence-table.sql
CREATE TABLE IF NOT EXISTS session_presence(
    id SERIAL PRIMARY KEY,
    campaign_id UUID NOT NULL,
    username VARCHAR(100) NOT NULL,
    joined_at TIMESTAMP DEFAULT NOW()
);