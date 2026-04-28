--liquibase formatted sql
--changeset Eduard:19

INSERT INTO maps (name, description, width, height, pic) VALUES ('Gothic Castle',
'Perched atop a jagged spire of obsidian rock, Raven’s Crag is a sprawling gothic masterpiece of shadow and stone.' ||
' This map features narrow, winding ramparts overlooking a mist-choked abyss and soaring cathedrals lit by flickering violet braziers.' ||
'Explore the Great Ossuary, navigate secret passages hidden behind rotting tapestries, and ascend the Star-Gazer’s Tower.' ||
' Every corridor echoes with forgotten whispers, making it the perfect atmospheric haunt for any dark fantasy campaign.',
                                                            30,
                                                            30, '../public/gothic-castle.png');
INSERT INTO maps (name, description, width, height, pic) VALUES ('Dark Fantasy Dungeon',
'Deep beneath the roots of a dead forest lies the Abyssal Oubliette, a dungeon forged from cold iron and weeping stone.'||
    ' This multi-level map plunges players into a claustrophobic nightmare of flooded torture chambers, sacrificial pits,' ||
' and shifting staircases that lead nowhere.',
                                                            30,
                                                            30,
                                                                 '../public/dark-fantasy-dungeon.png');
INSERT INTO maps (name, description, width, height, pic) VALUES ('Stormy Mountain',
'High above the clouds, The Tempest Spire is a brutal landscape of sheer cliffs and crumbling stone,'||
    ' perpetually battered by a supernatural storm. Lightning arcs across a charcoal sky, momentarily illuminating the narrow' ||
', ice-slicked bridges that connect jagged peaks.',
                                                                30,
                                                            30, '../public/stormy-mountain.png');