CREATE TABLE employers (
    employer_id SERIAL PRIMARY KEY,
    title VARCHAR (128),
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE vacancies (
vacancy_id SERIAL PRIMARY KEY,
    employer_id INTEGER,
    title VARCHAR (128) 
); 
