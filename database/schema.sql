-- Government Scheme Recommendation System
-- Run this file in MySQL Workbench or MySQL command line.

CREATE DATABASE IF NOT EXISTS government_scheme_db;
USE government_scheme_db;

-- Start with fresh practice tables each time this file is run.
DROP TABLE IF EXISTS Schemes;
DROP TABLE IF EXISTS States;
DROP TABLE IF EXISTS Genders;
DROP TABLE IF EXISTS Occupations;
DROP TABLE IF EXISTS Categories;

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
    -- NOT NULL: every scheme needs an age range and income limit range.
    min_age INT NOT NULL,
    max_age INT NOT NULL,
    min_income DOUBLE NOT NULL DEFAULT 0,
    max_income DOUBLE NOT NULL,
    -- DEFAULT 'ALL': use this when a scheme is open to every gender.
    gender VARCHAR(10) NOT NULL DEFAULT 'ALL',
    occupation VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    -- state: stores the name of the state
    state VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- Genders table
CREATE TABLE Genders (
    gender_id INT AUTO_INCREMENT PRIMARY KEY,
    gender_name VARCHAR(50) NOT NULL UNIQUE
);

-- Occupations table
CREATE TABLE Occupations (
    occupation_id INT AUTO_INCREMENT PRIMARY KEY,
    occupation_name VARCHAR(100) NOT NULL UNIQUE
);

-- Categories table
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert all Indian states and UTs.
INSERT INTO States (state_name) VALUES
('All India (Central)'),
('Andhra Pradesh'),
('Arunachal Pradesh'),
('Assam'),
('Bihar'),
('Chhattisgarh'),
('Goa'),
('Gujarat'),
('Haryana'),
('Himachal Pradesh'),
('Jharkhand'),
('Karnataka'),
('Kerala'),
('Madhya Pradesh'),
('Maharashtra'),
('Manipur'),
('Meghalaya'),
('Mizoram'),
('Nagaland'),
('Odisha'),
('Punjab'),
('Rajasthan'),
('Sikkim'),
('Tamil Nadu'),
('Telangana'),
('Tripura'),
('Uttar Pradesh'),
('Uttarakhand'),
('West Bengal'),
('Andaman and Nicobar Islands'),
('Chandigarh'),
('Dadra and Nagar Haveli and Daman and Diu'),
('Delhi'),
('Jammu and Kashmir'),
('Ladakh'),
('Lakshadweep'),
('Puducherry');

-- Insert Genders
INSERT INTO Genders (gender_name) VALUES
('Male'), ('Female'), ('Other');

-- Insert Occupations
INSERT INTO Occupations (occupation_name) VALUES
('Student'), ('Self-Employed'), ('Businessman'), ('Farmer'), ('Unemployed');

-- Insert Categories
INSERT INTO Categories (category_name) VALUES
('General'), ('OBC'), ('SC'), ('ST'), ('Others');

-- Insert four schemes. ANY means that rule applies to every value.
INSERT INTO Schemes (scheme_name, min_age, max_age, min_income, max_income, gender, occupation, category, state, description) VALUES
('PM Kisan Samman Nidhi', 18, 70, 0, 300000, 'ALL', 'Farmer', 'ANY', 'All India (Central)', 'Income support for farmers.'),
('Post Matric Scholarship', 15, 30, 0, 250000, 'ALL', 'Student', 'SC', 'All India (Central)', 'Scholarship for SC students.'),
('Maharashtra Girl Education', 15, 25, 0, 500000, 'Female', 'Student', 'ANY', 'Maharashtra', 'Support for girl students.'),
('Karnataka Farmer Support', 18, 65, 0, 400000, 'ALL', 'Farmer', 'ANY', 'Karnataka', 'Support for Karnataka farmers.');

-- CRUD INSERT: add one scheme when practising.
-- INSERT INTO Schemes (scheme_name, min_age, max_age, min_income, max_income, occupation, category, state, description)
-- VALUES ('Practice Scheme', 18, 60, 0, 200000, 'ANY', 'ANY', 'All India (Central)', 'Practice record.');

-- CRUD SELECT: view all schemes.
SELECT * FROM Schemes;

-- CRUD UPDATE: change one income limit when practising.
-- UPDATE Schemes SET max_income = 350000 WHERE scheme_id = 1;

-- CRUD DELETE: remove one scheme when practising.
-- DELETE FROM Schemes WHERE scheme_id = 4;

-- INNER JOIN: no longer needed since state is stored directly in Schemes, but here is a simple SELECT.
SELECT scheme_name, state
FROM Schemes;

-- WHERE: find schemes for a 22-year-old male SC student earning Rs. 200000.
SELECT scheme_name FROM Schemes
WHERE 22 BETWEEN min_age AND max_age
  AND 200000 BETWEEN min_income AND max_income
  AND (gender = 'ALL' OR gender = 'Male')
  AND (category = 'ANY' OR category = 'SC')
  AND (occupation = 'ANY' OR occupation = 'Student')
  AND state IN ('All India (Central)');

-- GROUP BY: count schemes in each state.
SELECT state, COUNT(scheme_id) AS total_schemes
FROM Schemes
GROUP BY state;

-- HAVING: show states with more than one scheme.
SELECT state, COUNT(scheme_id) AS total_schemes
FROM Schemes
GROUP BY state HAVING COUNT(scheme_id) > 1;

-- Aggregate functions: summarise the min and max income limits.
SELECT COUNT(*) AS total, AVG(min_income) AS avg_min_income, AVG(max_income) AS avg_max_income,
       MAX(max_income) AS highest_limit, MIN(min_income) AS lowest_limit
FROM Schemes;

-- Subquery: find the second highest max income limit.
SELECT MAX(max_income) AS second_highest
FROM Schemes WHERE max_income < (SELECT MAX(max_income) FROM Schemes);

-- EXISTS: check if there are any schemes in Maharashtra.
SELECT state FROM Schemes
WHERE state = 'Maharashtra' AND EXISTS (SELECT 1 FROM Schemes WHERE state = 'Maharashtra');

-- IN: find schemes available in Central or Maharashtra.
SELECT scheme_name FROM Schemes
WHERE state IN ('All India (Central)', 'Maharashtra');
