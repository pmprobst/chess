# Week 1 — Running the Test Suite

This document describes how to run the full CS 240 Chess test suite both from IntelliJ IDEA and from the command line.

---

## Prerequisites

- Java 21 installed
- Maven installed
- Project root contains `pom.xml`

Verify Java version:

```bash
java -version
```

Expected: Java 21

---

## Running Tests from IntelliJ IDEA

1. Open the Chess project in IntelliJ.
2. Open the **Maven** tool window:
   - View → Tool Windows → Maven
3. Expand:
   ```
   Lifecycle → test
   ```
4. Double-click **test**.

This runs the full test suite using Maven (`mvn test`).

---

## Running Tests from the Command Line

1. Open a terminal.
2. Navigate to the project root (directory containing `pom.xml`):

```bash
cd ~/Repos/chess
```

3. Ensure Java 21 is active:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

4. Run all tests:

```bash
mvn test
```

---

## Interpreting Results

- **BUILD SUCCESS**: All tests passed.
- **BUILD FAILURE** with `Not implemented`: Expected early in the project.
- Compilation or JDK errors indicate an environment problem.

Detailed test reports are located at:

```bash
shared/target/surefire-reports
```

---

## Notes

- Command-line test results are authoritative for grading.
- Tests are expected to fail until Phase 0 implementation is complete.


## Common Test Failures and Meanings

### `RuntimeException: Not implemented`

**Meaning:**  
This method is a required part of the project but has not yet been implemented.

**Typical Cause:**  
Starter code intentionally throws this exception in methods such as:
- `ChessBoard.resetBoard`
- Piece move generation methods

**Action:**  
Implement the method according to the project phase requirements. This failure is expected early in the course.

---

### `error: invalid target release: 21`

**Meaning:**  
Maven is attempting to compile using Java 21, but the active JDK is an older version.

**Typical Cause:**  
`JAVA_HOME` is not set to Java 21.

**Action:**  
Set the Java version explicitly:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

---

### `equals() returned false for equivalent objects`

**Meaning:**  
The `equals()` method does not correctly compare all fields that define object equality.

**Typical Cause:**  
- Comparing object references instead of values  
- Missing fields in the equality check

**Action:**  
Override `equals()` to compare all relevant fields defined by the class.

---

### `hashCode() returned different values for equivalent objects`

**Meaning:**  
`hashCode()` is inconsistent with `equals()`.

**Typical Cause:**  
- Hash code does not use the same fields as `equals()`
- Hash code is based on object identity

**Action:**  
Ensure `hashCode()` uses the same fields as `equals()` and produces consistent values.
