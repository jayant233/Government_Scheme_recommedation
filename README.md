# Government Scheme Recommendation System

An intentionally small Core Java, JDBC, MySQL, HTML/CSS, and Selenium project for **Deloitte ADMM (Application Development, Maintenance & Management)** interview preparation. It is designed to be explained confidently by a fresher, not to look like a production system.

---

## 1. Project Overview & Problem Statement

Citizens in India often struggle to identify government welfare schemes for which they qualify due to complex eligibility conditions spread across central and state government portals. 

The Java console application takes a `User` object, runs a SQL eligibility query against MySQL (`government_scheme_db`), and prints matching schemes. `SchemeDAO` also contains the small CRUD methods an admin would use to manage scheme records.

The HTML pages are deliberately static. They exist only for two beginner Selenium scripts. With Servlets, JSP, APIs, and JavaScript data fetching intentionally excluded, a static browser form cannot call the Java/JDBC code. Keeping the console and Selenium examples separate is simpler and honest.

---

## 2. Project Folder Structure

```text
GovernmentSchemeRecommendationSystem/
│
├── database/
│     schema.sql                         # MySQL database schema, seed data & interview queries
│
├── src/
│     Eligible.java                      # Interface (100% Abstraction)
│     Person.java                        # Abstract Class (Abstraction & Encapsulation)
│     User.java                          # Child Class extending Person & implementing Eligible
│     Admin.java                         # Child Class extending Person (Hierarchical Inheritance)
│     Scheme.java                        # Model POJO Class (Encapsulation)
│     SchemeDAO.java                     # DAO Class (JDBC, Method Overloading, Exception Handling)
│     SchemeNotFoundException.java       # Custom Checked Exception
│     Main.java                          # Entry Point (Runtime Polymorphism & Execution)
│
├── web/
│     index.html                         # Citizen recommendation form UI
│     admin.html                         # Admin management portal UI
│     results.html                       # HTML Table displaying eligible schemes
│     style.css                          # Vanilla CSS layout and visual styling
│
├── selenium/
│     HomeAndAdminPageTest.java          # Home title, admin navigation, and button check
│     RecommendationFormTest.java        # Form input and results-table check
│
└── README.md                            # Comprehensive documentation & Interview Guide
```

---

## 3. How This Project Answers Deloitte Interview Questions

| Interview Concept | Class / Query / Script Location | Explanation for Interviewer |
| :--- | :--- | :--- |
| **Interface** | `src/Eligible.java` | `Eligible` declares `showEligibleSchemes()`. |
| **Abstract Class** | `src/Person.java` | `Person` has private fields and the abstract `displayRole()` method. |
| **Inheritance and `super`** | `src/User.java`, `src/Admin.java` | Both child classes extend `Person` and call its constructor with `super(...)`. |
| **Overriding and runtime polymorphism** | `src/Main.java` | An `ArrayList<Person>` stores `User` and `Admin`, then calls their overridden `displayRole()` methods. |
| **Overloading** | `src/SchemeDAO.java` | The two `getEligibleSchemes()` methods accept either a `User` or the six eligibility values. |
| **Encapsulation** | `src/Scheme.java` | Fields are private and accessed through getters and setters. |
| **Custom exception** | `src/SchemeNotFoundException.java` | A checked exception is thrown when an ID is not found. |
| **JDBC cleanup** | `src/SchemeDAO.java` | Each JDBC query uses `try`, `catch`, and `finally`; `finally` closes its resources. |
| **SQL concepts** | `database/schema.sql` | Constraints, CRUD, JOIN, WHERE, GROUP BY, HAVING, aggregates, subquery, EXISTS, and IN. |
| **Selenium basics** | `selenium/` | Two scripts cover titles, ID/name/CSS/XPath locators, form input, clicking, table text, and visibility. |

---

## 4. Database Schema Setup

```sql
CREATE DATABASE IF NOT EXISTS government_scheme_db;
USE government_scheme_db;

CREATE TABLE States (
    state_id INT AUTO_INCREMENT PRIMARY KEY,
    state_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Schemes (
    scheme_id INT AUTO_INCREMENT PRIMARY KEY,
    scheme_name VARCHAR(150) NOT NULL UNIQUE,
    min_age INT NOT NULL,
    max_age INT NOT NULL,
    max_income DOUBLE NOT NULL,
    gender VARCHAR(10) NOT NULL DEFAULT 'ALL',
    occupation VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    state_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT fk_schemes_state FOREIGN KEY (state_id) REFERENCES States(state_id)
);
```

---

## 5. How to Compile & Run

### A. Run Database Script
Import and run `database/schema.sql` in MySQL Workbench or MySQL CLI:
```bash
mysql -u root -p < database/schema.sql
```

### B. Compile and Run Java Application

Before compiling, install a Java JDK, run MySQL, and place MySQL Connector/J in a `lib` folder. Update the `URL`, `USER`, and `PASSWORD` constants in `src/SchemeDAO.java` to match the local MySQL account.

Make sure `mysql-connector-j.jar` is on the classpath:
```bash
cd GovernmentSchemeRecommendationSystem
javac -d bin src/*.java
java -cp "bin;lib/mysql-connector-j-8.x.x.jar" Main
```

### C. Run a Selenium Script

Place Selenium's JAR files and the matching ChromeDriver in `lib`. Change the one `homeUrl` value at the top of each Selenium script if the project is moved. Then compile and run one script at a time:

```bash
javac -cp "lib/*" -d bin selenium/HomeAndAdminPageTest.java
java -cp "bin;lib/*" HomeAndAdminPageTest
```
o apply these core skills to Deloitte's ADMM client deliverables."*
