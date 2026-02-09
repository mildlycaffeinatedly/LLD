# Repository Version Management

This file documents versioning and module layout for this repository. It has been updated to reflect the current contents of the `LLD` folder.

## Current modules

The repository root `LLD/` currently contains these modules:

- `AmazonLocker/` — locker system design and source under `AmazonLocker/src/`.
- `TicTacToe/` — tic-tac-toe design and source under `TicTacToe/src/`.
- `RateLimiter/` — rate-limiter module (empty or in-development).

Each module uses a non-standard `src/` layout in this repo (sources live directly under `src/`). A parent `pom.xml` at the repository root aggregates modules and provides a common Java version.

## Build & Version guidance

- Use the root `pom.xml` to build all modules with Maven: `mvn -B package`.
- Module `pom.xml` files configure the module `sourceDirectory` to the existing `src/` folder.
- Compiled artifacts are kept out of source control; use the `out/`, `build/`, or `target/` directories for build output and add them to `.gitignore`.

## About the old Elevator content

The previous content in this file described an `Elevator` project (with `Elevator/` and `Elevator-v2/`). That project is not present in this repository tree. If you have a separate Elevator project, keep its version-management documentation inside that project's repository or a dedicated folder. If not needed, the old Elevator-specific content has been removed from this file.

## Recommendation

- Keep this `VERSION_MANAGEMENT.md` as a short, repo-level guide to modules and build practices.
- If you prefer module-specific versioning notes, add a `VERSION_MANAGEMENT.md` inside each module (for example `AmazonLocker/VERSION_MANAGEMENT.md`).

