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

---

## 6. 50 Deloitte-Style Java Interview Questions & Answers

1. **What is an interface in Java?**
   An interface defines a contract specifying what a class must do without defining how. All methods are implicitly `public` and `abstract`. (e.g., `Eligible.java`).
2. **Difference between Abstract Class and Interface?**
   Abstract classes can have instance variables and concrete methods; interfaces (pre-Java 8) only contain abstract methods and public static final constants.
3. **What is Encapsulation?**
   Wrapping data (variables) and code (methods) together into a single unit and restricting direct access by making fields `private` and exposing `getters/setters`.
4. **Why is `Person` declared abstract?**
   Because instantiating a generic `Person` makes no sense in our business domain; an individual must be either a citizen `User` or an `Admin`.
5. **What is single inheritance vs hierarchical inheritance?**
   Single inheritance: `User extends Person`. Hierarchical inheritance: both `User` and `Admin` extend `Person`.
6. **What does the `super` keyword do?**
   It calls the parent class constructor or methods (e.g., `super(id, name, age, gender)` inside `User` constructor).
7. **What is Method Overriding?**
   Redefining a parent class method in a child class with the exact same signature and return type (e.g., `displayRole()`).
8. **What is Method Overloading?**
   Declaring multiple methods in the same class with the same name but different parameter lists (e.g., `getEligibleSchemes()` in `SchemeDAO`).
9. **Difference between Compile-Time and Runtime Polymorphism?**
   Compile-time polymorphism is achieved via method overloading; runtime polymorphism is achieved via method overriding and parent class references.
10. **How did you demonstrate Runtime Polymorphism in your project?**
    By storing `User` and `Admin` objects inside an `ArrayList<Person>` and invoking `person.displayRole()` in a loop.
11. **What is a POJO?**
    Plain Old Java Object—a simple class containing private variables, constructors, and getters/setters (e.g., `Scheme.java`).
12. **Why use `ArrayList` over primitive arrays?**
    `ArrayList` is dynamically resizable and provides utility methods like `.add()`, `.get()`, and `.size()`.
13. **Difference between Checked and Unchecked Exceptions?**
    Checked exceptions extend `Exception` and must be handled at compile time; unchecked exceptions extend `RuntimeException`.
14. **Why is `SchemeNotFoundException` a checked exception?**
    Because it extends `Exception` directly, forcing calling code to handle missing scheme scenarios explicitly.
15. **Why use `try-catch-finally` in JDBC?**
    To handle potential `SQLException` errors in `catch` and guarantee that database resources (`Connection`, `PreparedStatement`, `ResultSet`) are closed in `finally`.
16. **Will code inside a `finally` block execute if an exception occurs?**
    Yes, `finally` executes regardless of whether an exception is thrown or caught.
17. **What is JDBC?**
    Java Database Connectivity—a standard Java API for connecting and executing queries against relational databases.
18. **Why use `PreparedStatement` over `Statement`?**
    `PreparedStatement` pre-compiles SQL queries, improves execution speed, and prevents SQL Injection attacks by parameterizing inputs.
19. **What does `executeUpdate()` return?**
    An integer representing the number of rows affected by `INSERT`, `UPDATE`, or `DELETE` statements.
20. **What does `executeQuery()` return?**
    A `ResultSet` object containing the tabular data generated by a `SELECT` query.
21. **What is the default value of object references in Java?**
    `null`.
22. **What is access modifier `private`?**
    Restricts access strictly within the declaring class.
23. **What is access modifier `public`?**
    Grants access from any class in any package.
24. **Can an abstract class have a constructor?**
    Yes! Abstract class constructors are called when a child class instance is initialized via `super(...)`.
25. **Can an interface have instance variables?**
    No, variables declared in an interface are implicitly `public static final` constants.
26. **Can a Java class extend multiple classes?**
    No, Java does not support multiple inheritance with classes to avoid the Diamond Problem.
27. **Can a Java class implement multiple interfaces?**
    Yes, Java supports multiple interface implementation.
28. **What is `@Override` annotation?**
    It informs the compiler that the method is intended to override a method in a superclass or interface.
29. **What happens if you don't close JDBC Connections?**
    It causes connection leaks, exhausting database pool resources and causing system downtime.
30. **What is `ResultSet.next()`?**
    Moves the cursor forward one row from its current position and returns `true` if a valid row exists.
31. **What is `DriverManager`?**
    A service class that manages a list of database drivers and establishes database connections via `getConnection()`.
32. **Why use getters and setters instead of public fields?**
    To control data access, enforce validation rules, and achieve Encapsulation.
33. **What is `this` keyword?**
    Refers to the current instance of the class inside a method or constructor.
34. **Difference between `throw` and `throws`?**
    `throw` is used to explicitly throw an exception instance inside a method; `throws` is used in a method signature to declare exceptions.
35. **What is exception propagation?**
    Uncaught exceptions travel up the call stack until handled by a matching `catch` block.
36. **Can we override a `private` method?**
    No, `private` methods are not visible to child classes.
37. **Can we override a `static` method?**
    No, `static` methods belong to the class and are hidden (method hiding), not overridden.
38. **What is the parent class of all Java classes?**
    `java.lang.Object`.
39. **How do you add elements to an `ArrayList`?**
    Using the `add()` method.
40. **What is the size method of `ArrayList`?**
    `size()` returns the number of elements in the list.
41. **What is a generic `ArrayList<Person>`?**
    Type-safe collection restricting elements to `Person` objects or its subclasses (`User`, `Admin`).
42. **Difference between `==` and `.equals()`?**
    `==` compares memory addresses (object references); `.equals()` compares string content or value equality.
43. **What is a getter and setter?**
    They read and update a private field. For example, `getAge()` reads a person's age.
44. **What is a constructor?**
    A special block of code called when an object is created to initialize instance attributes.
45. **Why are fields private in this project?**
    Private fields demonstrate encapsulation and prevent direct access from other classes.
46. **What is default constructor?**
    A no-argument constructor automatically provided by Java if no explicit constructor is defined.
47. **Can we instantiate an interface?**
    No, interfaces cannot be instantiated directly using `new`.
48. **What is `SQLException`?**
    It is a checked exception that JDBC can throw when database work fails.
49. **Why do we use `finally` in JDBC?**
    It closes database resources whether the query succeeds or fails.
50. **Why use `PreparedStatement`?**
    It puts values into `?` placeholders safely and keeps the SQL easy to read.

---

## 7. 30 SQL Interview Questions & Answers

1. **What is a Primary Key?**
   A column or set of columns that uniquely identifies each record in a table; cannot contain `NULL` values.
2. **What is a Foreign Key?**
   A column that enforces referential integrity between two tables by linking to a Primary Key in another table.
3. **Difference between `WHERE` and `HAVING`?**
   `WHERE` filters rows *before* grouping; `HAVING` filters grouped summaries *after* `GROUP BY` execution.
4. **What is an `INNER JOIN`?**
   Returns rows when there is at least one match in both joined tables.
5. **Difference between `INNER JOIN` and `LEFT JOIN`?**
   `INNER JOIN` returns matching rows only; `LEFT JOIN` returns all rows from the left table regardless of matches in the right table.
6. **What does `NOT NULL` constraint do?**
   Ensures that a column cannot store `NULL` (empty) values.
7. **What does `UNIQUE` constraint do?**
   Ensures all values in a column are distinct from one another.
8. **What does `DEFAULT` constraint do?**
   Assigns a pre-set value to a column if no value is provided during insertion.
9. **What is a Subquery?**
   A query nested inside another query (e.g., finding the 2nd highest income limit).
10. **What is the `GROUP BY` clause?**
    Groups rows sharing the same values into summary rows (e.g., scheme count by state).
11. **Name 5 Aggregate Functions in SQL.**
    `COUNT()`, `SUM()`, `AVG()`, `MAX()`, `MIN()`.
12. **What does the `IN` operator do?**
    Filters records that match any value within a specified set or subquery list.
13. **What does the `EXISTS` operator do?**
    Tests for the existence of records returned by a subquery (returns `TRUE` if subquery returns >0 rows).
14. **Difference between `DROP` and `DELETE`?**
    `DROP` deletes the table structure and data permanently; `DELETE` removes specific rows while preserving table structure.
15. **What is DDL vs DML?**
    DDL (Data Definition Language): `CREATE`, `DROP`, `ALTER`. DML (Data Manipulation Language): `INSERT`, `SELECT`, `UPDATE`, `DELETE`.
16. **How do you find the 2nd highest income limit in SQL?**
    `SELECT MAX(max_income) FROM Schemes WHERE max_income < (SELECT MAX(max_income) FROM Schemes);`
17. **What is Auto Increment?**
    Automatically generates a unique numeric value for new rows upon insertion.
18. **What is Referential Integrity?**
    Rule ensuring relationships between table rows remain valid and orphan foreign keys are prevented.
19. **What does the foreign key do in this project?**
    It prevents a scheme from using a state ID that does not exist in `States`.
20. **What is `BETWEEN` operator?**
    Filters values within an inclusive range (e.g., `22 BETWEEN min_age AND max_age`).
21. **Difference between `CHAR` and `VARCHAR`?**
    `CHAR` has fixed length; `VARCHAR` has variable dynamic length.
22. **What is a Database Schema?**
    The logical structure defining tables, fields, constraints, and relationships in a database.
23. **What is SQL Injection?**
    A code injection vulnerability where malicious SQL statements are inserted into entry fields.
24. **How do we prevent SQL Injection?**
    Using parameterized queries (`PreparedStatement` in JDBC).
25. **What does `LIKE` operator do?**
    Searches for specified patterns in a column using wildcards like `%`.
26. **What is `ORDER BY`?**
    Sorts query result sets in ascending (`ASC`) or descending (`DESC`) order.
27. **What is `COUNT(*)` vs `COUNT(column)`?**
    `COUNT(*)` counts all rows including NULLs; `COUNT(column)` counts non-NULL values only.
28. **What is a NULL value in SQL?**
    Represents missing, unknown, or unassigned data.
29. **Can a table have multiple Foreign Keys?**
    Yes, a table can have multiple Foreign Keys referencing different parent tables.
30. **Can a table have multiple Primary Keys?**
    No, a table can only have ONE Primary Key (though it can be a composite key spanning multiple columns).

---

## 8. 20 Selenium Interview Questions & Answers

1. **What is Selenium WebDriver?**
   A web automation tool that directly communicates with web browsers natively via browser drivers.
2. **Difference between `close()` and `quit()`?**
   `close()` closes the current focused browser window; `quit()` terminates all open driver windows and ends the WebDriver session.
3. **What does `findElement()` do?**
   It finds one web element, such as the name input or submit button.
4. **How do you enter text into an input box?**
   Using `element.sendKeys("text")`.
5. **How do you click a button or link?**
   Using `element.click()`.
6. **How do you retrieve page title?**
   Using `driver.getTitle()`.
7. **How do you extract inner text from an element?**
   Using `element.getText()`.
8. **Which locator strategies does this project use?**
   `By.id`, `By.name`, `By.xpath`, and `By.cssSelector`.
9. **What is XPath?**
   XML Path Language syntax used to navigate XML/HTML document structures and locate web elements.
10. **Why does the project use `//table[@id='resultsTable']`?**
    It is a short relative XPath that finds the results table by its ID.
11. **How does `selectByVisibleText()` work?**
     It chooses an option by the text the user can see, such as `Male`.
12. **Why store the results table in a `WebElement` variable?**
     The same table object is used for both `isDisplayed()` and `getText()`.
13. **What does `isDisplayed()` check?**
     It confirms that the Add Scheme button or results table is visible.
14. **Why use `By.cssSelector("#adminLink")`?**
     It demonstrates a short CSS selector for the Admin link.
15. **Why are there only two Selenium scripts?**
    Each script has one clear job, so the project stays easy to explain.
16. **How does a script open the page?**
    `driver.get(homeUrl)` opens the local HTML page in Chrome.
17. **What is `WebDriver driver = new ChromeDriver();`?**
    It creates a Chrome browser through the `WebDriver` interface reference.
18. **Why did you not use Page Object Model (POM) or TestNG?**
    To keep test scripts simple, transparent, and focused on core Selenium interaction methods without framework overhead.
19. **Why does each script use `try`, `catch`, and `finally`?**
     `catch` prints a simple error, and `finally` always closes the browser.
20. **Why are these pages static?**
    They are simple Selenium practice pages; the real SQL work stays in the Core Java console application.

---

## 9. 10-Minute Interview Explanation Script

**Interviewer:** *"Walk me through your resume project."*

**Candidate Pitch (10-Minute Script):**

> *"Good morning/afternoon. I would love to explain my project: **Government Scheme Recommendation System**.*

### 1. Project Context & Business Problem (1.5 Minutes)
> *"In India, citizens often miss out on government welfare benefits because eligibility criteria are scattered across multiple portals. I built a lightweight Core Java console application that receives basic citizen details and matches them against government scheme rules stored in MySQL. I also made simple static HTML pages only to practise Selenium browser automation."*

### 2. Architecture & Tech Stack (1.5 Minutes)
> *"I deliberately designed this project using pure **Core Java**, **JDBC**, **MySQL**, **HTML/CSS**, and **Selenium WebDriver** without relying on complex frameworks like Spring Boot or ORM tools. This allowed me to gain total mastery over foundational Java OOP principles, raw SQL queries, and basic browser automation scripts."*

### 3. Core Java Concepts & Design (3 Minutes)
> *"Let me highlight how I structured the Java codebase:
> - **Interface & Abstraction:** I defined an `Eligible` interface containing `showEligibleSchemes()`. I also created an abstract class `Person` with encapsulated fields (`id`, `name`, `age`, `gender`) and an abstract method `displayRole()`.
> - **Inheritance:** `User` and `Admin` extend `Person`. In their constructors, I use `super(...)` to pass common values to the parent constructor.
> - **Method Overriding & Runtime Polymorphism:** Both `User` and `Admin` override `displayRole()`. In `Main.java`, I store them together inside an `ArrayList<Person>` and call `displayRole()` in a loop. Java resolves which method to invoke at runtime dynamically.
> - **Method Overloading:** Inside `SchemeDAO`, I overloaded `getEligibleSchemes()`—one version takes a `User` object while the second takes primitive criteria parameters.
> - **Exception Handling & Custom Exception:** I created a checked exception `SchemeNotFoundException`. In `SchemeDAO`, if a user queries an invalid scheme ID, this exception is thrown. In JDBC operations, I use strict `try-catch-finally` blocks where `finally` safely closes `Connection`, `PreparedStatement`, and `ResultSet`."*

### 4. Database Design & SQL Logic (2 Minutes)
> *"On the database side, I created `government_scheme_db` with two tables: `States` and `Schemes`, linked by a `FOREIGN KEY`. I enforced `PRIMARY KEY`, `NOT NULL`, `UNIQUE`, and `DEFAULT` constraints. 
> Instead of writing heavy algorithmic logic in Java, the recommendation filter executes directly in SQL using multi-condition `WHERE` clauses (`BETWEEN`, `IN`, `EXISTS`). I also wrote analytical queries demonstrating `INNER JOIN`, `GROUP BY`, `HAVING` (states having >N schemes), aggregate functions (`COUNT`, `AVG`, `MAX`), and a subquery calculating the 2nd highest income limit."*

### 5. Frontend & Selenium Automation (2 Minutes)
> *"The frontend consists of simple HTML pages (`index.html`, `admin.html`, `results.html`) styled with Vanilla CSS. Every input element has unique `id` and `name` attributes.
> I wrote only 2 Selenium WebDriver scripts in Java using `ChromeDriver`, so I can explain each one. Together they demonstrate `By.id`, `By.name`, `By.xpath`, and `By.cssSelector`, as well as `sendKeys()`, `Select`, `click()`, `getText()`, `getTitle()`, `isDisplayed()`, `close()`, and `quit()`."*

> *"To summarize, this project gave me practical, hands-on command over Core Java OOPs, JDBC resource management, relational SQL query writing, and basic Selenium WebDriver test automation. I am eager to apply these core skills to Deloitte's ADMM client deliverables."*
