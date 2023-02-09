CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS public.students CASCADE;
DROP TABLE IF EXISTS public.groups CASCADE;

CREATE TABLE IF NOT EXISTS public.groups
(
group_id integer NOT NULL GENERATED ALWAYS AS IDENTITY
( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ) PRIMARY KEY,
name character varying (255) NOT NULL);

CREATE TABLE IF NOT EXISTS public.students (
student_id integer NOT NULL GENERATED ALWAYS AS IDENTITY
( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
first_name character varying(20)  NOT NULL,
last_name character varying(20)  NOT NULL,
group_id integer NOT NULL DEFAULT 1,
PRIMARY KEY (student_id),
CONSTRAINT fk_group_student FOREIGN KEY (group_id)
REFERENCES public.groups (group_id)
ON UPDATE NO ACTION
ON DELETE NO ACTION);

INSERT INTO public.groups (name) values ('first');
INSERT INTO public.groups (name) values ('second');

INSERT INTO public.students (first_name, last_name, group_id) values ('Chris', 'Martin', 1);
INSERT INTO public.students (first_name, last_name, group_id) values ('Mari', 'Osvald', 2);