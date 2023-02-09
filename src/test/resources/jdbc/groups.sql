CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS public.groups CASCADE;

CREATE TABLE IF NOT EXISTS public.groups
(
group_id integer NOT NULL GENERATED ALWAYS AS IDENTITY
( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ) PRIMARY KEY,
name character varying (255) NOT NULL);


INSERT INTO public.groups (name) values ('first');
INSERT INTO public.groups (name) values ('second');
INSERT INTO public.groups (name) values ('third');