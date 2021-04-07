DROP TABLE IF EXISTS public.game_state CASCADE;
DROP TABLE IF EXISTS public.player CASCADE;
DROP TABLE IF EXISTS public.player_inventory CASCADE;


CREATE TABLE public.game_state (
    id serial NOT NULL,
    current_map integer NOT NULL,
    map1 text NOT NULL,
    map2 text NOT NULL,
    map3 text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

CREATE TABLE public.player (
    id serial NOT NULL,
    player_name text NOT NULL,
    strength text NOT NULL,
    health integer NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL
);

CREATE TABLE public.player_inventory (
    id serial NOT NULL,
    player_id integer NOT NULL,
    item_name text NOT NULL,
    item_type text NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD PRIMARY KEY (id);
ALTER TABLE ONLY public.player
    ADD PRIMARY KEY (id);
ALTER TABLE ONLY public.player_inventory
    ADD PRIMARY KEY (id);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
ALTER TABLE ONLY public.player_inventory
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
