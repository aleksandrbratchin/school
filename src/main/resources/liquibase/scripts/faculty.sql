--liquibase formatted sql

-- changeset aleksbratchin:create_faculty
CREATE TABLE faculty (
	id uuid PRIMARY KEY,
    color varchar(255) NOT NULL,
    name varchar(255) NOT NULL
);

-- changeset aleksbratchin:faculty_unique_name_color
ALTER TABLE faculty ADD CONSTRAINT faculty_unique_name_color UNIQUE (name, color);

-- changeset aleksbratchin:faculty_index_color_name
CREATE INDEX faculty_index_color_name ON faculty (name, color)