--liquibase formatted sql

--------------------------------------------------------------------------------
-- STEP 3: Add UUID columns to core tables
--------------------------------------------------------------------------------
--changeset Eduard:3-add-uuid-columns
ALTER TABLE campaigns ADD COLUMN id_uuid UUID DEFAULT gen_random_uuid() NOT NULL;
ALTER TABLE campaign_actors ADD COLUMN campaign_uuid UUID;
ALTER TABLE campaign_members ADD COLUMN campaign_uuid UUID;

--------------------------------------------------------------------------------
-- STEP 4: Populate new UUID foreign key columns
--------------------------------------------------------------------------------
--changeset Eduard:4-populate-fk-columns
-- Update campaign_members...
UPDATE campaign_members cm
SET campaign_uuid = (
    SELECT c.id_uuid FROM campaigns c WHERE c.id = cm.campaign
);

-- Update campaign_actors...
UPDATE campaign_actors ca
SET campaign_uuid = (
    SELECT c.id_uuid FROM campaigns c WHERE c.id = ca.campaign
);

--------------------------------------------------------------------------------
-- STEP 4a: Add UUID column to 'turns'
--------------------------------------------------------------------------------
--changeset Eduard:4a-add-uuid-to-turns
ALTER TABLE turns ADD COLUMN campaign_uuid UUID;

--------------------------------------------------------------------------------
-- STEP 4b: Populate UUID FK in 'turns'
--------------------------------------------------------------------------------
--changeset Eduard:4b-populate-turns-fk
UPDATE turns t
SET campaign_uuid = (
    SELECT c.id_uuid FROM campaigns c WHERE c.id = t.campaign
);

--------------------------------------------------------------------------------
-- STEP 4c: Add UUID columns to action tables
--------------------------------------------------------------------------------
--changeset Eduard:4c-add-uuid-to-action-tables
ALTER TABLE attack_actions ADD COLUMN campaign_id_uuid UUID;
ALTER TABLE movement_actions ADD COLUMN campaign_id_uuid UUID;

--------------------------------------------------------------------------------
-- STEP 4d: Populate UUID FKs in action tables
--------------------------------------------------------------------------------
--changeset Eduard:4d-populate-action-tables-fk
-- Update attack_actions with the new campaign UUIDs.
UPDATE attack_actions aa
SET campaign_id_uuid = (
    SELECT c.id_uuid FROM campaigns c WHERE c.id = aa.campaign_id
);

-- Update movement_actions with the new campaign UUIDs.
UPDATE movement_actions ma
SET campaign_id_uuid = (
    SELECT c.id_uuid FROM campaigns c WHERE c.id = ma.campaign_id
);

--------------------------------------------------------------------------------
-- STEP 5: Drop old structure (constraints and integer columns)
--------------------------------------------------------------------------------
--changeset Eduard:5-drop-old-structure
-- Drop deepest FKs first
ALTER TABLE attack_actions DROP CONSTRAINT IF EXISTS attack_actions_turn_index_campaign_id_fkey;
ALTER TABLE movement_actions DROP CONSTRAINT IF EXISTS movement_actions_turn_index_campaign_id_fkey;

-- Next-level FKs
ALTER TABLE campaign_actors DROP CONSTRAINT IF EXISTS campaign_actors_campaign_fkey;
ALTER TABLE campaign_members DROP CONSTRAINT IF EXISTS campaign_members_campaign_fkey;
ALTER TABLE turns DROP CONSTRAINT IF EXISTS turns_campaign_fkey;

-- Drop dependent PKs
ALTER TABLE turns DROP CONSTRAINT IF EXISTS turns_pkey;
ALTER TABLE campaigns DROP CONSTRAINT IF EXISTS campaigns_pkey;

-- Drop old integer columns
ALTER TABLE campaigns DROP COLUMN IF EXISTS id;
ALTER TABLE campaign_actors DROP COLUMN IF EXISTS campaign;
ALTER TABLE campaign_members DROP COLUMN IF EXISTS campaign;
ALTER TABLE turns DROP COLUMN IF EXISTS campaign;
ALTER TABLE attack_actions DROP COLUMN IF EXISTS campaign_id;
ALTER TABLE movement_actions DROP COLUMN IF EXISTS campaign_id;

--------------------------------------------------------------------------------
-- STEP 6: Rename new UUID columns to replace old ones
--------------------------------------------------------------------------------
--changeset Eduard:6-rename-new-columns
ALTER TABLE campaigns RENAME COLUMN id_uuid TO id;
ALTER TABLE campaign_actors RENAME COLUMN campaign_uuid TO campaign;
ALTER TABLE campaign_members RENAME COLUMN campaign_uuid TO campaign;
ALTER TABLE turns RENAME COLUMN campaign_uuid TO campaign;
ALTER TABLE attack_actions RENAME COLUMN campaign_id_uuid TO campaign_id;
ALTER TABLE movement_actions RENAME COLUMN campaign_id_uuid TO campaign_id;

--------------------------------------------------------------------------------
-- STEP 7: Recreate constraints and primary keys
--------------------------------------------------------------------------------
--changeset Eduard:7-recreate-constraints

-- Primary keys
ALTER TABLE campaigns ADD CONSTRAINT campaigns_pkey PRIMARY KEY (id);
ALTER TABLE turns ADD CONSTRAINT turns_pkey PRIMARY KEY (index, campaign);
ALTER TABLE campaign_members ADD CONSTRAINT pk_campaign_members PRIMARY KEY (owner, campaign);


-- Set FK columns to NOT NULL
ALTER TABLE campaign_members ALTER COLUMN campaign SET NOT NULL;
ALTER TABLE campaign_actors ALTER COLUMN campaign SET NOT NULL;
ALTER TABLE turns ALTER COLUMN campaign SET NOT NULL;
ALTER TABLE attack_actions ALTER COLUMN campaign_id SET NOT NULL;
ALTER TABLE movement_actions ALTER COLUMN campaign_id SET NOT NULL;

-- Recreate foreign keys
ALTER TABLE campaign_members
  ADD CONSTRAINT fk_campaign_members_campaign
  FOREIGN KEY (campaign)
  REFERENCES campaigns(id)
  ON DELETE CASCADE;

ALTER TABLE campaign_actors
  ADD CONSTRAINT fk_campaign_actors_campaign
  FOREIGN KEY (campaign)
  REFERENCES campaigns(id)
  ON DELETE CASCADE;

ALTER TABLE turns
  ADD CONSTRAINT turns_campaign_fkey
  FOREIGN KEY (campaign)
  REFERENCES campaigns(id);

ALTER TABLE attack_actions
  ADD CONSTRAINT attack_actions_turn_index_campaign_id_fkey
  FOREIGN KEY (turn_index, campaign_id)
  REFERENCES turns(index, campaign);

ALTER TABLE movement_actions
  ADD CONSTRAINT movement_actions_turn_index_campaign_id_fkey
  FOREIGN KEY (turn_index, campaign_id)
  REFERENCES turns(index, campaign);
