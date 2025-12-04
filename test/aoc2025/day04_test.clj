(ns aoc2025.day04-test
  (:require [clojure.test :refer :all]
            [aoc2025.day04 :refer :all]
            [aoc2025.grid :as grid]))

(deftest count-rolls-test
  (testing "isolated roll with no neighbors"
    (let [g (grid/grid-from-lines ["..."
                                     ".@."
                                     "..."])]
      (is (= 0 (count-rolls g 1 1)))))

  (testing "roll with one neighbor"
    (let [g (grid/grid-from-lines ["@.."
                                     ".@."
                                     "..."])]
      (is (= 1 (count-rolls g 1 1)))))

  (testing "roll with three neighbors"
    (let [g (grid/grid-from-lines ["@@@"
                                     ".@."
                                     "..."])]
      (is (= 3 (count-rolls g 1 1)))))

  (testing "roll with five neighbors"
    (let [g (grid/grid-from-lines ["@@@"
                                     "@@@"
                                     "..."])]
      (is (= 5 (count-rolls g 1 1)))))

  (testing "roll with eight neighbors"
    (let [g (grid/grid-from-lines ["@@@"
                                     "@@@"
                                     "@@@"])]
      (is (= 8 (count-rolls g 1 1)))))

  (testing "roll on edge of grid"
    (let [g (grid/grid-from-lines ["@@@"
                                     "@@@"
                                     "..."])]
      (is (= 3 (count-rolls g 0 0)))))

  (testing "roll in corner of grid"
    (let [g (grid/grid-from-lines ["@@"
                                     "@@"])]
      (is (= 3 (count-rolls g 0 0))))))

(deftest accessible-test
  (testing "isolated roll is accessible"
    (let [g (grid/grid-from-lines ["..."
                                     ".@."
                                     "..."])]
      (is (accessible? g [1 1]))))

  (testing "roll with 3 neighbors is accessible"
    (let [g (grid/grid-from-lines ["@@@"
                                     ".@."
                                     "..."])]
      (is (accessible? g [1 1]))))

  (testing "roll with 4 neighbors is not accessible"
    (let [g (grid/grid-from-lines ["@@@"
                                     "@@@"
                                     "..."])]
      (is (not (accessible? g [1 1])))))

  (testing "roll with 8 neighbors is not accessible"
    (let [g (grid/grid-from-lines ["@@@"
                                     "@@@"
                                     "@@@"])]
      (is (not (accessible? g [1 1])))))

  (testing "corner roll with 3 neighbors is accessible"
    (let [g (grid/grid-from-lines ["@@"
                                     "@@"])]
      (is (accessible? g [0 0])))))

(deftest part1-example-test
  (testing "part1 with example input from problem"
    (let [input "..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@."
          grid (parse-input input)]
      (is (= 13 (part1 grid))))))