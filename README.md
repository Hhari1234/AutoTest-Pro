# AutoTest Pro

A professional QA automation framework for the SauceDemo demo site using Java, Selenium WebDriver, TestNG, Maven, and ExtentReports.

## Features

- Page Object Model (POM) architecture
- Selenium WebDriver automation for Chrome
- TestNG execution and result aggregation
- Config-driven browser setup
- HTML and ExtentReports generation
- Screenshot capture on failed tests
- Results dashboard powered by real Surefire XML output

## Project Structure

- `src/main/java/config` – configuration loader
- `src/main/java/driver` – WebDriver factory
- `src/main/java/pages` – page objects
- `src/main/java/utils` – wait, logging, and reporting helpers
- `src/test/java/base` – shared test setup and teardown
- `src/test/java/tests` – automated UI test cases
- `src/test/resources` – TestNG suite and config files
- `dashboard/` – React + TypeScript results dashboard
- `scripts/` – data-generation utilities for dashboard inputs

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- Google Chrome installed

## Local Execution

Run the Java test suite from the project root:

```bash
mvn test
```

If Maven is not on your PATH:

```powershell
cd "C:\Users\harir\OneDrive\Desktop\AutoTest Pro"
& "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" test
```

Generate the dashboard data from the latest Surefire XML report:

```bash
cd dashboard
npm install
npm run build
```

The dashboard reads `target/surefire-reports/testng-results.xml` and renders a live summary with class-level and test-level details.

## Reports

After execution, reports are emitted to:

- `reports/` – ExtentReports and HTML output
- `screenshots/` – failed test screenshots
- `target/surefire-reports/` – machine-readable TestNG XML and summary files
- `dashboard/public/results.json` – dashboard-ready JSON generated from the XML report

## CI Workflow

The GitHub Actions workflow runs `mvn clean test -Dheadless=true`, publishes the Java artifacts, and then builds the dashboard using the generated XML report.

## Main Files

- `pom.xml` – Maven configuration and dependencies
- `src/test/resources/testng.xml` – TestNG suite definition
- `src/test/resources/config.properties` – runtime configuration
- `scripts/generate-dashboard-data.mjs` – transforms the XML report into dashboard JSON
- `dashboard/src/App.tsx` – dashboard UI
