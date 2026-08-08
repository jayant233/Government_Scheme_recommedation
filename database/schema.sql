-- Government Scheme Recommendation System
-- Run this file in MySQL Workbench or MySQL command line.

CREATE DATABASE IF NOT EXISTS government_scheme_db;
USE government_scheme_db;

-- Start with fresh practice tables each time this file is run.
DROP TABLE IF EXISTS Schemes;
DROP TABLE IF EXISTS States;

-- States stores the state name used by a scheme.
CREATE TABLE States (
    -- PRIMARY KEY: gives every state one unique ID.
    state_id INT AUTO_INCREMENT PRIMARY KEY,
    -- NOT NULL: a state must have a name. UNIQUE: names cannot repeat.
    state_name VARCHAR(100) NOT NULL UNIQUE
);

-- Schemes stores the eligibility rules for each government scheme.
CREATE TABLE Schemes (
    -- PRIMARY KEY: gives every scheme one unique ID.
    scheme_id INT AUTO_INCREMENT PRIMARY KEY,
    -- NOT NULL and UNIQUE: every scheme needs a different name.
    scheme_name VARCHAR(150) NOT NULL UNIQUE,
    -- NOT NULL: every scheme needs an age range and income limit.
    min_age INT NOT NULL,
    max_age INT NOT NULL,
    max_income DOUBLE NOT NULL,
    -- DEFAULT 'ALL': use this when a scheme is open to every gender.
    gender VARCHAR(10) NOT NULL DEFAULT 'ALL',
    occupation VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    -- FOREIGN KEY: the state ID must already exist in States.
    state_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT fk_scheme_state FOREIGN KEY (state_id) REFERENCES States(state_id)
);

-- Insert three simple practice states.
INSERT INTO States (state_name) VALUES
('All India (Central)'),
('Maharashtra'),
('Karnataka');

-- Insert four schemes. ANY means that rule applies to every value.
INSERT INTO Schemes (scheme_name, min_age, max_age, max_income, gender, occupation, category, state_id, description) VALUES
('PM Kisan Samman Nidhi', 18, 70, 300000, 'ALL', 'Farmer', 'ANY', 1, 'Income support for farmers.'),
('Post Matric Scholarship', 15, 30, 250000, 'ALL', 'Student', 'SC', 1, 'Scholarship for SC students.'),
('Maharashtra Girl Education', 15, 25, 500000, 'Female', 'Student', 'ANY', 2, 'Support for girl students.'),
('Karnataka Farmer Support', 18, 65, 400000, 'ALL', 'Farmer', 'ANY', 3, 'Support for Karnataka farmers.');

-- CRUD INSERT: add one scheme when practising.
-- INSERT INTO Schemes (scheme_name, min_age, max_age, max_income, occupation, category, state_id, description)
-- VALUES ('Practice Scheme', 18, 60, 200000, 'ANY', 'ANY', 1, 'Practice record.');

-- CRUD SELECT: view all schemes.
SELECT * FROM Schemes;

-- CRUD UPDATE: change one income limit when practising.
-- UPDATE Schemes SET max_income = 350000 WHERE scheme_id = 1;

-- CRUD DELETE: remove one scheme when practising.
-- DELETE FROM Schemes WHERE scheme_id = 4;

-- INNER JOIN: show each scheme with its state name.
SELECT s.scheme_name, st.state_name
FROM Schemes s INNER JOIN States st ON s.state_id = st.state_id;

-- WHERE: find schemes for a 22-year-old male SC student earning Rs. 200000.
SELECT scheme_name FROM Schemes
WHERE 22 BETWEEN min_age AND max_age
  AND 200000 <= max_income
  AND (gender = 'ALL' OR gender = 'Male')
  AND (category = 'ANY' OR category = 'SC')
  AND (occupation = 'ANY' OR occupation = 'Student')
  AND state_id IN (SELECT state_id FROM States WHERE state_name = 'All India (Central)');

-- GROUP BY: count schemes in each state.
SELECT st.state_name, COUNT(s.scheme_id) AS total_schemes
FROM States st LEFT JOIN Schemes s ON st.state_id = s.state_id
GROUP BY st.state_name;

-- HAVING: show states with more than one scheme.
SELECT st.state_name, COUNT(s.scheme_id) AS total_schemes
FROM States st INNER JOIN Schemes s ON st.state_id = s.state_id
GROUP BY st.state_name HAVING COUNT(s.scheme_id) > 1;

-- Aggregate functions: summarise the income limits.
SELECT COUNT(*) AS total, AVG(max_income) AS average_limit, MAX(max_income) AS highest_limit,
       MIN(max_income) AS lowest_limit, SUM(max_income) AS income_limit_sum
FROM Schemes;

-- Subquery: find the second highest income limit.
SELECT MAX(max_income) AS second_highest
FROM Schemes WHERE max_income < (SELECT MAX(max_income) FROM Schemes);

-- EXISTS: find states that have at least one scheme.
SELECT state_name FROM States st
WHERE EXISTS (SELECT 1 FROM Schemes s WHERE s.state_id = st.state_id);

-- IN: find schemes available in Central or Maharashtra.
SELECT scheme_name FROM Schemes
WHERE state_id IN (1, 2);
