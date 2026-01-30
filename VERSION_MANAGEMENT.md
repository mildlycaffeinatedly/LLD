# Elevator Project - Version Management

This document explains the structure for maintaining multiple versions of the Elevator System project.

## Directory Structure

```
LLD/
├── Elevator/           (Original Version - Keep Stable)
│   ├── pom.xml
│   ├── README.md
│   ├── src/
│   │   ├── main/java/com/elevator/
│   │   └── test/java/com/elevator/
│   └── target/
│
└── Elevator-v2/        (Version 2 - Development Space)
    ├── pom.xml
    ├── README.md
    └── src/
        ├── main/java/com/elevator/
        └── test/java/com/elevator/
```

## Usage

### Build Original Version (Elevator)
```bash
cd /Users/Sushant/Documents/LLD/Elevator
mvn clean install
mvn exec:java -Dexec.mainClass="com.elevator.Main"
```

### Build Version 2 (Elevator-v2)
```bash
cd /Users/Sushant/Documents/LLD/Elevator-v2
mvn clean install
mvn exec:java -Dexec.mainClass="com.elevator.Main"
```

## Development Workflow

1. **Elevator/** - Keep this as your stable/reference implementation
2. **Elevator-v2/** - Make improvements and changes here
   - Refactor code
   - Add new features
   - Implement optimizations
   - Run tests independently

## Switching Between Versions

Each directory has its own:
- `pom.xml` - Independent Maven configuration
- `src/` - Completely separate source code
- `target/` - Separate build output
- Dependencies - Isolated per version

This ensures changes in one version don't affect the other.

## Notes

- Both versions use Java 11 (can be upgraded independently)
- Each version can have different dependencies
- Test suites are independent
- Build artifacts go to separate target/ directories
