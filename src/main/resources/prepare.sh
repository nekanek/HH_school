createdb hh_hw_javadb
psql hh_hw_javadb

CREATE TABLE employers (
    employer_id SERIAL PRIMARY KEY,
    title VARCHAR (128),
    registration_date TIMESTAMP NOT NULL
);