(ns aoc2025.day10-test
  (:require [clojure.test :refer :all]
            [aoc2025.day10 :refer :all]))

(deftest parse-machine-test
  (testing "Parses a simple machine configuration"
    (let [input "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
          result (parse-machine input)]
      (is (= [\. \# \# \.] (:indicator result)))
      (is (= [[3] [1 3] [2] [2 3] [0 2] [0 1]] (:buttons result)))
      (is (= [3 5 4 7] (:joltage result)))))

  (testing "Parses machine with longer indicator"
    (let [input "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
          result (parse-machine input)]
      (is (= [\. \. \. \# \.] (:indicator result)))
      (is (= [[0 2 3 4] [2 3] [0 4] [0 1 2] [1 2 3 4]] (:buttons result)))
      (is (= [7 5 12 7 2] (:joltage result))))))

(deftest indicator->bitfield-test
  (testing "Converts indicator to bitfield (position i → bit i)"
    (is (= 2r0110 (indicator->bitfield [\. \# \# \.])))
    (is (= 2r01000 (indicator->bitfield [\. \. \. \# \.])))
    (is (= 2r101110 (indicator->bitfield [\. \# \# \# \. \#])))))

(deftest button->bitfield-test
  (testing "Converts button indices to bitfield"
    (is (= 2r1000 (button->bitfield [3])))
    (is (= 2r1010 (button->bitfield [1 3])))
    (is (= 2r101 (button->bitfield [0 2])))
    (is (= 2r11101 (button->bitfield [0 2 3 4])))))

(deftest permutations-test
  (testing "Generates all subsets (power set)"
    (is (= '(()) (permutations [])))
    (is (= '(() (1)) (permutations [1])))
    (is (= '(() (2) (1) (1 2)) (permutations [1 2])))
    (is (= '(() (3) (2) (2 3) (1) (1 3) (1 2) (1 2 3))
           (permutations [1 2 3]))))

  (testing "Power set has correct size"
    (is (= 16 (count (permutations [1 2 3 4]))))
    (is (= 32 (count (permutations [1 2 3 4 5]))))
    (is (= 1024 (count (permutations (range 10)))))))

(deftest min-presses-test
  (testing "Machine 1: minimum 2 presses"
    (let [machine (parse-machine "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")]
      (is (= 2 (min-presses machine)))))

  (testing "Machine 2: minimum 3 presses"
    (let [machine (parse-machine "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")]
      (is (= 3 (min-presses machine)))))

  (testing "Machine 3: minimum 2 presses"
    (let [machine (parse-machine "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")]
      (is (= 2 (min-presses machine))))))

(deftest build-augmented-matrix-test
  (testing "Builds augmented matrix from machine data"
    (let [machine {:buttons [[3] [1 3] [2] [2 3] [0 2] [0 1]]
                   :joltage [3 5 4 7]}
          matrix (build-augmented-matrix machine)]
      (is (= [[0 0 0 0 1 1 3]   ; counter 0: buttons 4,5
              [0 1 0 0 0 1 5]   ; counter 1: buttons 1,5
              [0 0 1 1 1 0 4]   ; counter 2: buttons 2,3,4
              [1 1 0 1 0 0 7]]  ; counter 3: buttons 0,1,3
             matrix)))))

(deftest part1-test
  (testing "Solves example from problem description"
    (let [input "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
          parsed (parse-input input)]
      (is (= 7 (part1 parsed))))))