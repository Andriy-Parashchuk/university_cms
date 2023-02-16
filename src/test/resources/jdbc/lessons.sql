CREATE SCHEMA IF NOT EXISTS test;

DROP TABLE IF EXISTS public.lessons CASCADE;

CREATE TABLE IF NOT EXISTS public.lessons
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying  NOT NULL,
    teacher_id integer NOT NULL,
    group_id integer NOT NULL,
    "time" timestamp with time zone NOT NULL,
    audience integer NOT NULL,
    CONSTRAINT lessons_pkey PRIMARY KEY (id),
    CONSTRAINT lessons_group FOREIGN KEY (group_id)
        REFERENCES public.groups (id) 
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT lessons_teacher FOREIGN KEY (teacher_id)
        REFERENCES public.teachers (id) 
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


insert into lessons (name, teacher_id, group_id, "time", audience) 
values ('Math', 2, 1, '2023-02-10 10:30:00+02', 305);

insert into lessons (name, teacher_id, group_id, "time", audience) 
values ('Biology', 1, 2, '2023-02-11 12:00:00+02', 203);
