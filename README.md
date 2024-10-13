# Java Maven Playwright Framework

## Overview
This repository contains a Java-based test framework using Maven and Playwright for both UI and API testing. It is designed to provide a seamless testing experience with automated testing capabilities for web applications and APIs.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Generating Reports](#generating-reports)

## Prerequisites
Before you begin, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK) 17 or Higher**
   - Download and install the JDK from [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or [Adoptium](https://adoptium.net/).

2. **Apache Maven 3.6 or Higher**
   - Download and install Maven from the [Apache Maven website](https://maven.apache.org/download.cgi).

3. **Integrated Development Environment (IDE)**
   - For ease of development, you may use:
     - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
     - [Eclipse](https://www.eclipse.org/)

4. **Browser Dependencies for Playwright**
   - Playwright will automatically download the required browser binaries when running the tests for the first time.

5. **Git (Optional)**
   - If you are cloning the repository, download Git from the [official Git website](https://git-scm.com/downloads).


## Installation
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
  
2. **Navigate to the project directory**
    cd KontronPetClinic

3. **Install Dependencies**
    mvn install

## Running Tests
    mvn test

## Generating Reports
    mvn clean test
    Reports are generated using ExtentReports and are stored in the root directory as ExtentReport.html.
