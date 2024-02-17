--liquibase formatted sql

-- changeset aleksbratchin:create_student
CREATE TABLE student (
	id uuid PRIMARY KEY,
	age int4,
	name varchar(255),
    faculty_id uuid
);

-- changeset aleksbratchin:student_fk_faculty
ALTER TABLE student ADD CONSTRAINT student_fk_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id);

-- changeset aleksbratchin:student_constraints
ALTER TABLE student ADD CONSTRAINT student_min_age CHECK (age >= 11);
ALTER TABLE student ADD CONSTRAINT student_name_is_not_empty CHECK (name != '');
ALTER TABLE student ADD CONSTRAINT student_unique_name UNIQUE (name);
ALTER TABLE student ALTER COLUMN name SET NOT NULL;
ALTER TABLE student ALTER COLUMN age SET NOT NULL;
ALTER TABLE student ALTER COLUMN age SET DEFAULT 11;

-- changeset aleksbratchin:student_index_name
CREATE INDEX student_index_name ON student (name)
