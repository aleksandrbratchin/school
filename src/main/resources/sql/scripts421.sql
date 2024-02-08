ALTER TABLE student add CHECK (age >= 16);
ALTER TABLE student ADD CHECK (name != '');
ALTER TABLE student ADD CONSTRAINT some_name UNIQUE (name);
ALTER TABLE student ALTER COLUMN name SET NOT NULL;
ALTER TABLE student ALTER COLUMN age SET DEFAULT 18;
ALTER TABLE faculty ADD UNIQUE (name, color);