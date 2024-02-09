--liquibase formatted sql

-- changeset aleksbratchin:create_avatar
CREATE TABLE avatar (
	id uuid PRIMARY KEY,
    data bytea NULL,
    file_path varchar(255) NULL,
    file_size int8 NOT NULL,
    media_type varchar(255) NULL,
    student_id uuid NOT NULL
);

ALTER TABLE avatar ADD CONSTRAINT avatar_unique_student_id UNIQUE (student_id);

-- changeset aleksbratchin:avatar_fk_student
ALTER TABLE avatar ADD CONSTRAINT avatar_fk_student FOREIGN KEY (student_id) REFERENCES student(id);