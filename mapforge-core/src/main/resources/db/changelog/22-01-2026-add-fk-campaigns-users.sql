--liquibase sql

--changeset Eduard:17

ALTER TABLE public.campaigns
    ADD COLUMN creator_id INTEGER;

ALTER TABLE public.campaigns
    ADD CONSTRAINT fk_campaigns_users
        FOREIGN KEY (creator_id)
            REFERENCES public.users (id)
            ON DELETE CASCADE;