--liquibase formatted sql

--changeset Eduard:007-add-default-npcs
INSERT INTO npcs (armor, speed, weapon_damage, type) VALUES
                                                         (15, 30, 8,  'humanoid'::public.npc_type),
                                                         (12, 40, 12, 'beast'::public.npc_type),
                                                         (18, 20, 15, 'undead'::public.npc_type);