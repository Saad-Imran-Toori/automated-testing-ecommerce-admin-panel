# Automated Testing – E‑Commerce Admin Panel

A full‑stack e‑commerce **admin panel** (PHP REST API + HTML/CSS/JS front end) built as a vehicle for an
**automated software testing** assignment. The repository bundles the application together with four
independent test layers — PHP API tests, JUnit 5 + HtmlUnit unit tests, Selenium WebDriver GUI tests, and
a Selenium Grid + Docker cross‑browser parallel suite — plus a Postman collection and captured test‑result
screenshots.

---

## 📁 Repository Structure

```
automated-testing-ecommerce-admin-panel/
├── database.sql                 # Schema + seed data (database: ecommerce_db)
│
├── backend/                     # PHP REST API
│   ├── api/
│   │   ├── auth.php             # Admin signup / login (PDO, password_hash)
│   │   └── products.php         # Products CRUD (GET/POST/PUT/DELETE + validation)
│   ├── config/
│   │   └── db_connect.php       # PDO database connection
│   └── tests/                   # PHPUnit API/DB integration tests
│       ├── AuthAPITest.php
│       ├── ProductsAPITest.php
│       └── DatabaseTest.php
│
├── frontend/                    # Static admin UI (vanilla HTML/CSS/JS)
│   ├── index.html               # Landing page
│   ├── admin_login.html
│   ├── admin_signup.html
│   ├── admin_dashboard.html     # Product management dashboard
│   ├── css/style.css
│   └── js/admin.js
│
├── tests/                       # Java test suites (Maven)
│   ├── pom.xml                  # Dependencies: JUnit 5, HtmlUnit, Selenium, TestNG, WebDriverManager
│   ├── unit/                    # JUnit 5 + HtmlUnit (headless – no browser)
│   │   ├── AdminSideTest.java          # 18 tests
│   │   └── CompleteWebsiteTest.java    # page‑structure / navigation tests
│   ├── selenium/                # Selenium WebDriver GUI tests (real Chrome)
│   │   └── EcommerceAdminPanelTest.java
│   ├── grid/                    # Selenium Grid + Docker parallel (cross‑browser)
│   │   ├── SeleniumGridParallelTest.java
│   │   ├── ParallelGridTest.java
│   │   ├── testng.xml
│   │   ├── docker-compose.yml          # Grid hub + Chrome/Firefox nodes
│   │   └── demo/                       # Minimal standalone Grid demo
│   │       ├── ParallelGridTest.java
│   │       └── testng.xml
│   └── postman/                 # Postman API test collection
│       ├── ecommerce_api.postman_collection.json   # importable, matches the API
│       ├── postman work.docx                        # original Postman write‑up
│       └── README.md
│
├── screenshots/
│   ├── selenium/                # GUI test result screenshots
│   ├── postman/                 # Postman request/response screenshots (13)
│   └── grid-report/             # TestNG HTML report (Grid parallel run)
│
└── docs/                        # Assignment reports & notes
    ├── Project Report.docx      # Final project report
    ├── Admin side testing report.docx
    ├── PHP UnitTesting Commands.docx
    ├── postman work.docx
    ├── Unit Test Report.docx / Unit Testing Report.docx / .pdf
    └── test-suite-notes/        # Supplementary generated notes (.md)
```

---

## 🧰 Tech Stack

| Layer            | Technology                                                            |
|------------------|-----------------------------------------------------------------------|
| Backend API      | PHP 8 (PDO), MySQL/MariaDB                                             |
| Front end        | HTML5, CSS3, vanilla JavaScript, Font Awesome                         |
| API tests        | PHPUnit                                                                |
| Unit tests       | JUnit 5 (Jupiter 5.10.2) + HtmlUnit 4.21.0                            |
| GUI tests        | Selenium WebDriver 4.15.0 + ChromeDriver + WebDriverManager 5.6.3    |
| Parallel / Grid  | Selenium Grid 4 (Docker) + TestNG 7.8.0                              |
| API collection   | Postman (Collection v2.1)                                            |
| Build            | Maven (Java 21)                                                       |

---

## 🚀 Application Setup (XAMPP)

The test suites expect the app served at **`http://localhost/ecommerce_api/`**.

1. **Install XAMPP** and start **Apache** and **MySQL**.
2. **Deploy the app** – copy `backend/` and `frontend/` into your web root inside a folder named
   `ecommerce_api`, so that:
   - `http://localhost/ecommerce_api/frontend/index.html` loads the landing page
   - `http://localhost/ecommerce_api/backend/api/products.php` returns JSON

   > The PHP includes use the relative path `../config/db_connect.php`, so keep the
   > `backend/api` + `backend/config` layout intact.
3. **Create the database** – import `database.sql` (creates the `ecommerce_db` database, the
   `admins` and `products` tables, and seed data):
   ```bash
   # phpMyAdmin → Import → database.sql, or:
   mysql -u root < database.sql
   ```
4. **Create an admin account** via `admin_signup.html`.
   > ⚠️ The seeded `admin@example.com` row in `database.sql` contains a malformed bcrypt hash and
   > **cannot be logged into** — register a fresh admin through the signup page instead.
5. DB credentials live in `backend/config/db_connect.php` (defaults: host `localhost`, db
   `ecommerce_db`, user `root`, empty password — the XAMPP defaults).

---

## 🔌 REST API Reference

Base URL: `http://localhost/ecommerce_api/backend/api`

### Auth — `auth.php` (POST, JSON body)
| Action  | Body                                                                   | Success response                       |
|---------|------------------------------------------------------------------------|----------------------------------------|
| signup  | `{ "action":"signup","firstName","lastName","email","password" }`      | `{ "status":"success" }`               |
| login   | `{ "action":"login","email","password" }`                              | `{ "status":"success","name","email" }`|

### Products — `products.php`
| Method | URL                  | Purpose            | Notes                                       |
|--------|----------------------|--------------------|---------------------------------------------|
| GET    | `products.php`       | List all products  | returns `data[]` + `count`                  |
| GET    | `products.php?id=1`  | Get one product    | error payload if missing                    |
| POST   | `products.php`       | Create product     | validates name required, price > 0, qty ≥ 0 |
| PUT    | `products.php?id=1`  | Update product     | partial update; same validation             |
| DELETE | `products.php?id=1`  | Delete product     |                                             |

All responses are JSON of the form `{ "status": "success" | "error", ... }`.

---

## 🧪 Running the Tests

### 1. PHP API tests (PHPUnit) — `backend/tests/`
Hit the live API, so Apache + MySQL must be running.
```bash
# from the backend/tests directory (requires phpunit on PATH or phpunit.phar)
phpunit AuthAPITest.php
phpunit ProductsAPITest.php
phpunit DatabaseTest.php
```
Covers: DB connectivity, admin signup + login, and product GET/POST/PUT/DELETE.

### 2. JUnit 5 + HtmlUnit unit tests — `tests/unit/`
Headless (no real browser). `AdminSideTest` contains **18** tests across login, signup, dashboard,
add‑product validation and navigation; `CompleteWebsiteTest` adds page‑structure / CSS / navigation
checks.
```bash
mvn test -Dtest=AdminSideTest
mvn test -Dtest=CompleteWebsiteTest
```

### 3. Selenium WebDriver GUI tests — `tests/selenium/`
Drives a **real Chrome** browser (ChromeDriver auto‑managed by WebDriverManager). Covers the login,
signup, dashboard, product‑management and navigation flows, and **auto‑captures a screenshot on
failure** into `test-screenshots/` (sample results are in [`screenshots/selenium/`](screenshots/selenium/)).
```bash
mvn test -Dtest=EcommerceAdminPanelTest
```

### 4. Selenium Grid + Docker parallel tests — `tests/grid/`
Runs the same scenarios on **Chrome and Firefox in parallel** against a Dockerised Selenium Grid.

```bash
# 1. Start the Grid (hub + chrome + firefox nodes)
cd tests/grid
docker compose up -d
#    Grid console: http://localhost:4444/ui

# 2. Run the parallel TestNG suite (from the Maven project)
mvn test -DsuiteXmlFile=tests/grid/testng.xml
```
The tests connect to the hub at `http://localhost:4444`; from inside the containers the app is reached
via `host.docker.internal`. A sample parallel‑run TestNG report is in
[`screenshots/grid-report/`](screenshots/grid-report/).

> **Note on the Java layout.** Files are grouped by *test type* for readability. The Java sources declare
> `package com.ecommerce.tests;` (the Grid *demo* uses `package gridtest;`). To build with Maven, place the
> sources under `src/test/java/com/ecommerce/tests/` (the original Eclipse project layout) — `pom.xml` is
> provided in `tests/`. Alternatively, import into Eclipse and run **Run As → JUnit/TestNG Test**.

### 5. Postman collection — `tests/postman/`
Import [`ecommerce_api.postman_collection.json`](tests/postman/ecommerce_api.postman_collection.json) into
Postman. It contains ready‑to‑run requests (with assertions) for every auth and product endpoint. The
actual manual Postman testing evidence is in `tests/postman/postman work.docx` and
[`screenshots/postman/`](screenshots/postman/). See [`tests/postman/README.md`](tests/postman/README.md).

---

## 📸 Screenshots

- **`screenshots/selenium/`** — result captures from the Selenium GUI runs (timestamped per test).
- **`screenshots/postman/`** — request/response captures from the manual Postman API testing (13 images).
- **`screenshots/grid-report/`** — the generated TestNG HTML report from a Grid parallel run; open
  `index.html` to browse pass/fail per browser.

---

## 📋 Prerequisites Summary

- XAMPP (Apache + MySQL) or any PHP 8 + MySQL/MariaDB stack
- Java JDK 21 and Maven 3.6+
- Google Chrome (and Firefox for the Grid suite)
- Docker Desktop (for the Selenium Grid suite)
- PHPUnit (for the PHP API tests) and Postman (for the collection)

---

## ✍️ Author

**Saad Imran Toori** — Automated Software Testing assignment.
