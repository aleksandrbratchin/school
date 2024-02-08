CREATE TABLE car (
	id SERIAL PRIMARY KEY,
	brand varchar(255) CHECK(name !='') NOT NULL,
	model varchar(255) CHECK(name !='') NOT NULL,
	price float8 NOT NULL,
	unique(brand, model)
);
CREATE TABLE person (
	id SERIAL PRIMARY KEY,
	name varchar(255) CHECK(name !='') NOT NULL,
	age int8 DEFAULT 18 CHECK(age >= 18 AND age < 100) NOT NULL,
	is_driver_license boolean DEFAULT false NOT NULL,
	car_id INTEGER REFERENCES car(id) NOT NULL
);
drop table person;
drop table car;