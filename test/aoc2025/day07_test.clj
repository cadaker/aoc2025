(ns aoc2025.day07-test
  (:require [clojure.test :refer :all]
            [aoc2025.day07 :refer :all]))

(def example-input
  ".......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............")

(deftest propagate-beams-test
  (testing "No splitters - beams pass through straight down"
    (is (= #{3 5 7}
           (propagate-beams #{3 5 7} #{}))))

  (testing "Beam hits splitter - splits to columns left and right"
    (is (= #{2 4}
           (propagate-beams #{3} #{3}))))

  (testing "Multiple beams, one hits splitter"
    (is (= #{2 4 5 9}
           (propagate-beams #{3 5 9} #{3}))))

  (testing "Multiple beams hit splitters"
    (is (= #{1 3 4 6}
           (propagate-beams #{2 5} #{2 5}))))

  (testing "Beams can converge to same column"
    (is (= #{2 4 6}
           (propagate-beams #{3 5} #{3 5})))))

(deftest example-test
  (testing "Part 1: Example from problem description"
    (is (= 21 (part1 (parse-input example-input)))))

  (testing "Part 2: Quantum timelines for example"
    (is (= 40 (part2 (parse-input example-input))))))