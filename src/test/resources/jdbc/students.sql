CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS public.students CASCADE;

CREATE TABLE IF NOT EXISTS public.students (
id integer NOT NULL GENERATED ALWAYS AS IDENTITY
( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
first_name character varying(20)  NOT NULL,
last_name character varying(20)  NOT NULL,
email character varying,
group_id integer NOT NULL DEFAULT 1,
PRIMARY KEY (id),
CONSTRAINT fk_group_student FOREIGN KEY (group_id)
REFERENCES public.groups (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION);

INSERT INTO public.groups (name) values ('first');
INSERT INTO public.groups (name) values ('second');

INSERT INTO public.students (first_name, last_name, email, group_id) values ('Chris', 'Martin', 'test@test.test', 1);
INSERT INTO public.students (first_name, last_name, email, group_id) values ('Mari', 'Osvald', 'test@test.test', 2);