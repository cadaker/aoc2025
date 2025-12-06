# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Important: User Preferences for Advent of Code

**DO NOT help with puzzle solutions or algorithms unless explicitly asked.** The user wants to solve the theory and approach themselves. Only provide implementation help after the user has explained their solution approach. If the user is stuck and explicitly asks for help, then you may assist with the problem-solving aspect.

## Project Overview

This is an Advent of Code 2025 solution repository using Clojure and Leiningen. Each day's puzzle is solved in a separate namespace following a consistent pattern.

## General style guidelines

- For solution commits, we can use short commit messages like "Day 1 part 1 solved"

## Architecture

**Entry Point**: `src/aoc2025/core.clj` contains the `-main` function that dynamically loads and executes daily solutions. It takes a day number as argument, constructs the namespace `aoc2025.dayXX` (zero-padded), requires it, and calls its `solve` function.

**Daily Solution Pattern**: Each day follows this structure (see `src/aoc2025/day01.clj` as template):
- Namespace: `aoc2025.dayXX` where XX is zero-padded (e.g., `day01`, `day15`)
- Functions:
  - `read-input`: reads from `resources/dayXX.txt`
  - `parse-input`: transforms raw input into usable data structure
  - `part1`: solves part 1 of the puzzle
  - `part2`: solves part 2 of the puzzle
  - `solve`: orchestrates the solution and prints results
- The `solve` function is the public interface called by the main dispatcher

**Input Files**: Puzzle inputs go in `resources/dayXX.txt` and are gitignored (per Advent of Code's request not to share inputs).

## Common Commands

Run a specific day's solution:
```bash
lein run <day>
```

Run all tests:
```bash
lein test
```

Run tests for a specific day:
```bash
lein test aoc2025.day01-test
```

Run a specific test:
```bash
lein test :only aoc2025.day01-test/example-helper-test
```

Start REPL for interactive development:
```bash
lein repl
```

## Testing

**Test Structure**: Test files mirror source files - `test/aoc2025/dayXX_test.clj` tests `src/aoc2025/dayXX.clj`.

**What to Test**: Write unit tests for individual helper functions using hardcoded test data within the test file. Tests should not read from input files or test against the actual puzzle input.

**Shared Utilities**: If you create shared utility functions used across multiple days, place them in `src/aoc2025/utils.clj` and test them in `test/aoc2025/utils_test.clj`.

## Creating a New Day

1. Copy `src/aoc2025/day01.clj` to `src/aoc2025/dayXX.clj` (zero-padded number)
2. Update the namespace declaration to match the file
3. Update the `read-input` function to read from the correct resource file
4. The input file is `resources/dayXX.txt`. The user will copy this file for each day.
5. Implement `parse-input`, `part1`, and `part2` functions
6. Update the `solve` function to print the correct day number
7. Optionally create `test/aoc2025/dayXX_test.clj` for testing helper functions
