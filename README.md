# AutoTest Pro

A professional QA automation testing framework built for the SauceDemo demo site using Java, Selenium WebDriver, TestNG, Maven, and ExtentReports.

## Features

- Page Object Model (POM) architecture
- Selenium WebDriver automation for Chrome
- TestNG suite and test lifecycle management
- Config-driven browser setup
- HTML reporting with ExtentReports
- Screenshot capture on failed tests
- Cross-browser-ready structure for future extension

## Project Structure

- `src/main/java/config` – configuration loader
- `src/main/java/driver` – WebDriver factory
- `src/main/java/pages` – page objects
- `src/main/java/utils` – utilities for waits, logging, screenshots, and reporting
- `src/test/java/base` – shared test setup
- `src/test/java/tests` – automated test cases
- `src/test/resources` – TestNG suite and config files

## Prerequisites

- Java 17+
- Maven 3.9+
- Google Chrome installed

## Local Execution

From the project root:

```bash
mvn test
```

If Maven is not on your PATH, use the full Maven binary path, for example:

```powershell
cd "C:\Users\harir\OneDrive\Desktop\AutoTest Pro"
& "C:\maven\apache-maven-3.9.9\bin\mvn.cmd" test
```

## Optional Headless Mode

The framework supports a headless Chrome configuration through the config file in `src/test/resources/config.properties`.

## Reports

After execution, reports are generated in the `reports/` folder and screenshots for failures are stored in `screenshots/`.

## Main Files

- `pom.xml` – Maven configuration and dependencies
- `src/test/resources/testng.xml` – TestNG suite definition
- `src/test/resources/config.properties` – runtime configuration
