# Advent of Code 2025

Solutions for [Advent of Code 2025](https://adventofcode.com/2025) written in Clojure.

## Structure

- Each day's solution is in `src/aoc2025/dayXX.clj`
- Input files go in `resources/dayXX.txt` (gitignored)

## Usage

Run a specific day's solution:

```bash
lein run <day>
```

For example, to run day 1:

```bash
lein run 1
```

## Adding a New Day

1. Create `src/aoc2025/dayXX.clj` (use `day01.clj` as a template)
2. Add your puzzle input to `resources/dayXX.txt`
3. Implement `part1` and `part2` functions
4. Run with `lein run XX`

## REPL Development

Start a REPL for interactive development:

```bash
lein repl
```

## License

Copyright © 2025 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
