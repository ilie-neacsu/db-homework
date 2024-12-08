# DB Homework Project

This repository contains two Java exercises demonstrating different programming concepts.

## Project Structure

```plaintext
db-homework/            # Root project (parent)
├── exercise-one/       # Module 1: Thread synchronization
│   ├── src/
│   │   ├── main/java   # Main source code
│   │   └── test/java   # Test source code
│   └── pom.xml         # Module 1 POM
├── exercise-two/       # Module 2: Number pair matching
│   ├── src/
│   │   ├── main/java   # Main source code
│   │   └── test/java   # Test source code
│   └── pom.xml         # Module 2 POM
└── pom.xml             # Parent POM
```

- `exercise-one/`: Implementation of a ping-pong thread synchronization problem
- `exercise-two/`: Implementation of a number pair matching algorithm

## Prerequisites

- Java 21
- Maven 3.x

## Building the Project

To build all modules, run from the root directory:

```sh
mvn clean install
```

## Running the Exercises

### Exercise One

This exercise demonstrates thread synchronization by alternating "ping" and "pong" outputs between two threads for 5 seconds.

To run Exercise One:

```sh
mvn -pl exercise-one exec:java -Dexec.mainClass="com.example.exerciseone.Main"
```

For more details, see [Exercise One README](exercise-one/README.md)

### Exercise Two: Number Pair Matching

This exercise implements an algorithm to find the maximum sum of two numbers that share their first and last digits.

To run Exercise Two:

```sh
mvn -pl exercise-two exec:java -Dexec.mainClass="com.example.exercisetwo.Main"
```

For more details, see [Exercise Two README](exercise-two/README.md)

## Running Tests

To run tests for all modules:

```sh
mvn test
```

To run tests for a specific module:

```sh
mvn -pl exercise-one test
mvn -pl exercise-two test
```

## Project Configuration

- Java Version: 21
- Build Tool: Maven 3.x
- Test Framework: JUnit 5.x
