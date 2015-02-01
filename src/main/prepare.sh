createdb hh_hw_javadb
psql hh_hw_javadb

CREATE TABLE employers (
    employer_id SERIAL PRIMARY KEY,
    title VARCHAR (128),
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE vacancies (
	vacancy_id SERIAL PRIMARY KEY,
    employer_id INTEGER,
    title VARCHAR (128),
    post_date TIMESTAMP NOT NULL
);


title, employer_id, post_date