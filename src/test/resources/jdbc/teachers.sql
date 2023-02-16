CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS public.teachers CASCADE;

CREATE TABLE IF NOT EXISTS public.teachers
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    audience integer,
    department character varying, 
    CONSTRAINT teachers_pkey PRIMARY KEY (id)
);

 
INSERT INTO public.teachers (first_name, last_name, audience , department) 
values ('Chris', 'Martin', 203, 'Biology');

INSERT INTO public.teachers (first_name, last_name, audience , department) 
values ('Mari', 'Osvald', 304, 'Math');
