# Government Scheme Recommendation System

A full-stack Java web application that helps Indian citizens discover government welfare schemes they are eligible for. Built with **Spring Boot**, **Thymeleaf**, **JDBC**, and **MySQL**, and tested end-to-end with **Selenium WebDriver** and **JUnit 5**.

---

## Table of Contents

- [Problem Statement](#problem-statement)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [OOP & Core Java Concepts Demonstrated](#oop--core-java-concepts-demonstrated)
- [SQL Concepts Demonstrated](#sql-concepts-demonstrated)
- [Selenium Concepts Demonstrated](#selenium-concepts-demonstrated)
- [Screenshots](#screenshots)

---

## Problem Statement

Citizens in India often struggle to identify government welfare schemes for which they qualify. Eligibility conditions—based on age, income, gender, occupation, social category, and state—are spread across central and state government portals, making discovery difficult.

This application provides a single interface where a citizen fills in their personal details and instantly receives a list of matching schemes from the database.

---

## Features

### Citizen Portal (`/`)
- Dynamic form with dropdowns populated from the database (gender, occupation, category, state)
- Eligibility engine matches user input against scheme rules using parameterized SQL
- Results displayed in-page as an HTML table

### Admin Portal (`/admin`)
- View all schemes in a tabular format
- **Add** a new scheme via form submission
- **Update** an existing scheme by ID
- **Delete** a scheme by ID
- Success and error flash messages after every operation

### Console Demonstration (`Main.java`)
- Standalone `main()` method demonstrating runtime polymorphism, interface usage, and JDBC eligibility queries outside of the web context

---

## Tech Stack

| Layer | Technology |
| :--- | :--- |
| Language | Java 11 |
| Framework | Spring Boot 2.7.18 |
| Templating | Thymeleaf |
| Database | MySQL 8.x |
| JDBC Driver | MySQL Connector/J 8.0.33 |
| Build Tool | Maven |
| Testing | JUnit 5, Selenium 4.18.1, WebDriverManager 5.7.0 |

---

## Architecture

```
┌──────────────────────────────────────────────────────┐
│                     Browser                          │
│         (index.html / admin.html via Thymeleaf)      │
└──────────────────┬───────────────────────────────────┘
                   │  HTTP GET / POST
                   ▼
┌──────────────────────────────────────────────────────┐
│              SchemeController.java                    │
│     @GetMapping("/")  @GetMapping("/results")        │
│     @GetMapping("/admin")  @PostMapping("/admin/*")  │
└──────────────────┬───────────────────────────────────┘
                   │  method calls
                   ▼
┌──────────────────────────────────────────────────────┐
│                SchemeDAO.java                         │
│   getEligibleSchemes()  getAllSchemes()  addScheme()  │
│   updateScheme()  deleteScheme()  getGenders() ...   │
└──────────────────┬───────────────────────────────────┘
                   │  JDBC (PreparedStatement)
                   ▼
┌──────────────────────────────────────────────────────┐
│              MySQL — government_scheme_db             │
│     Schemes · States · Genders · Occupations ·       │
│                    Categories                        │
└──────────────────────────────────────────────────────┘
```

---

## Project Structure

```
Government_Scheme_Recommendation/
│
├── pom.xml                                        # Maven build config (Spring Boot parent)
│
├── database/
│   └── schema.sql                                 # DDL, seed data, and practice SQL queries
│
├── src/main/java/com/govscheme/
│   ├── Application.java                           # Spring Boot entry point
│   ├── SchemeController.java                      # MVC controller (all routes)
│   ├── SchemeDAO.java                             # Data access layer (raw JDBC)
│   ├── Scheme.java                                # Model POJO (encapsulation)
│   ├── Person.java                                # Abstract class (abstraction)
│   ├── User.java                                  # Extends Person, implements Eligible
│   ├── Admin.java                                 # Extends Person (hierarchical inheritance)
│   ├── Eligible.java                              # Interface (100% abstraction)
│   ├── SchemeNotFoundException.java               # Custom checked exception
│   └── Main.java                                  # Console demo (polymorphism)
│
├── src/main/resources/
│   ├── templates/
│   │   ├── index.html                             # Citizen recommendation form + results
│   │   └── admin.html                             # Admin CRUD portal
│   └── static/
│       └── style.css                              # Application stylesheet
│
├── src/test/java/
│   ├── SchemeDAOTest.java                         # JUnit 5 — DAO unit tests
│   └── com/example/mainGR/selenium/
│       ├── BaseTest.java                          # WebDriver setup/teardown (BeforeEach/AfterEach)
│       ├── HomePageTest.java                      # TC01–TC04: title, URL, form fields, navigation
│       ├── RecommendationFormTest.java            # TC05–TC09: form submission, results, validation
│       └── AdminPortalTest.java                   # TC10–TC15: CRUD operations, table display


        
```

---

## Database Schema

The `database/schema.sql` file creates and seeds the following tables:

```
┌────────────────┐     ┌────────────────┐
│    Schemes     │     │    States      │
├────────────────┤     ├────────────────┤
│ scheme_id (PK) │     │ state_id  (PK) │
│ scheme_name    │     │ state_name     │
│ min_age        │     └────────────────┘
│ max_age        │
│ min_income     │     ┌────────────────┐
│ max_income     │     │   Genders      │
│ gender         │     ├────────────────┤
│ occupation     │     │ gender_id (PK) │
│ category       │     │ gender_name    │
│ state          │     └────────────────┘
│ description    │
└────────────────┘     ┌────────────────┐     ┌────────────────┐
                       │  Occupations   │     │  Categories    │
                       ├────────────────┤     ├────────────────┤
                       │ occupation_id  │     │ category_id    │
                       │ occupation_name│     │ category_name  │
                       └────────────────┘     └────────────────┘
```

**Seed data includes:** 4 schemes (PM Kisan, Post Matric Scholarship, Maharashtra Girl Education, Karnataka Farmer Support), all 36 Indian states/UTs, and lookup values for gender, occupation, and social category.

---

## Getting Started

### Prerequisites

- **Java 11+** (JDK)
- **Maven 3.6+**
- **MySQL 8.x** running locally

### 1. Set Up the Database

```bash
mysql -u root -p < database/schema.sql
```

This creates the `government_scheme_db` database, tables, and seed data.

### 2. Configure Database Credentials

Open `src/main/java/com/govscheme/SchemeDAO.java` and update the connection constants to match your MySQL setup:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/government_scheme_db";
private static final String USER     = "root";
private static final String PASSWORD = "your_password";
```

### 3. Build & Run the Web Application

```bash
mvn spring-boot:run
```

Open **http://localhost:8080** in your browser.

### 4. Run the Console Demo (Optional)

```bash
mvn compile exec:java -Dexec.mainClass="com.govscheme.Main"
```

---

## Running Tests

### JUnit DAO Tests

```bash
mvn test -Dtest=SchemeDAOTest
```

> **Note:** Requires a running MySQL instance with the seeded database.

### Selenium Tests (JUnit)

1. Start the application first: `mvn spring-boot:run`
2. In a separate terminal, run the Selenium test suite:

```bash
mvn test -Dtest="com.example.mainGR.selenium.*"
```

ChromeDriver is managed automatically via WebDriverManager — no manual driver download needed.

**Test Coverage:**

| Test ID | Class | Validates |
| :--- | :--- | :--- |
| TC01 | `HomePageTest` | Home page title |
| TC02 | `HomePageTest` | Home page URL |
| TC03 | `HomePageTest` | All form fields are displayed |
| TC04 | `HomePageTest` | Navigation to admin portal |
| TC05 | `RecommendationFormTest` | Form submission with valid data |
| TC06 | `RecommendationFormTest` | Results section is displayed |
| TC07 | `RecommendationFormTest` | Eligible schemes table appears |
| TC08 | `RecommendationFormTest` | Empty-field validation (HTML5 required) |
| TC09 | `RecommendationFormTest` | Dropdown selection persistence |
| TC10 | `AdminPortalTest` | Admin page loads with action buttons |
| TC11 | `AdminPortalTest` | Add scheme form visibility |
| TC12 | `AdminPortalTest` | Adding a new scheme end-to-end |
| TC13 | `AdminPortalTest` | Updating an existing scheme |
| TC14 | `AdminPortalTest` | All schemes displayed in table |
| TC15 | `AdminPortalTest` | Deleting a scheme |

---

## OOP & Core Java Concepts Demonstrated

| Concept | Where | How |
| :--- | :--- | :--- |
| **Interface** | `Eligible.java` | Declares `showEligibleSchemes()` — 100% abstraction |
| **Abstract class** | `Person.java` | Private fields + abstract `displayRole()` method |
| **Inheritance (`extends`)** | `User.java`, `Admin.java` | Both extend `Person`; `super(...)` calls the parent constructor |
| **Interface implementation** | `User.java` | `User extends Person implements Eligible` — multiple type inheritance |
| **Runtime polymorphism** | `Main.java` | `ArrayList<Person>` holds `User` and `Admin`, calling overridden `displayRole()` |
| **Method overloading** | `SchemeDAO.java` | `getEligibleSchemes(User)` vs `getEligibleSchemes(int, double, String, ...)` |
| **Encapsulation** | `Scheme.java`, `Person.java` | Private fields with public getters/setters |
| **Custom exception** | `SchemeNotFoundException.java` | Checked exception extending `Exception` |
| **Exception handling** | `SchemeDAO.java` | `try` / `catch` / `finally` with explicit JDBC resource cleanup |
| **Collections** | `Main.java`, `SchemeDAO.java` | `ArrayList<Person>`, `ArrayList<Scheme>`, `ArrayList<String>` |

---

## SQL Concepts Demonstrated

All queries are in `database/schema.sql`:

| Concept | Query |
| :--- | :--- |
| DDL (`CREATE`, `DROP`) | Table creation with constraints |
| DML (`INSERT`, `UPDATE`, `DELETE`) | CRUD operations on Schemes |
| `SELECT *` | Retrieve all schemes |
| `WHERE` with `BETWEEN`, `AND`, `OR`, `IN` | Eligibility matching query |
| `GROUP BY` | Count schemes per state |
| `HAVING` | States with more than one scheme |
| Aggregate functions | `COUNT`, `AVG`, `MAX`, `MIN` |
| Subquery | Find second-highest income limit |
| `EXISTS` | Check if schemes exist in a state |
| `IN` | Filter schemes by multiple states |
| `PRIMARY KEY`, `NOT NULL`, `UNIQUE`, `DEFAULT` | Column-level constraints |

---

## Selenium Concepts Demonstrated

| Concept | Where |
| :--- | :--- |
| Locators: `By.id`, `By.name`, `By.cssSelector`, `By.xpath` | All test classes |
| `Select` class for dropdowns | `RecommendationFormTest`, `AdminPortalTest` |
| `sendKeys()`, `click()`, `getText()` | Form interaction across all tests |
| `isDisplayed()` | Element visibility assertions |
| Explicit waits (`WebDriverWait`, `ExpectedConditions`) | `RecommendationFormTest`, `AdminPortalTest` |
| Implicit waits | `BaseTest.java` — 10-second implicit wait |
| `@BeforeEach` / `@AfterEach` lifecycle | `BaseTest.java` — setup and teardown |
| Page navigation & URL assertions | `HomePageTest` (TC02, TC04) |
| WebDriverManager auto-setup | `BaseTest.java` — no manual driver download |

---

## Screenshots

> *Start the application with `mvn spring-boot:run` and visit http://localhost:8080 to see the citizen portal, then navigate to /admin for the management portal.*
