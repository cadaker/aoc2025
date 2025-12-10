(ns aoc2025.gauss-test
  (:require [clojure.test :refer :all]
            [aoc2025.gauss :refer :all]))

(deftest find-first-nonzero-test
  (testing "Finds first non-zero element in column"
    (let [matrix [[0 1 2]
                  [0 3 4]
                  [5 6 7]]]
      (is (= 2 (find-first-nonzero matrix 0 0)))   ; row 2 has first non-zero in col 0
      (is (= 0 (find-first-nonzero matrix 0 1)))   ; row 0 has non-zero in col 1
      (is (= 1 (find-first-nonzero matrix 1 1))))) ; starting from row 1, row 1 has non-zero

  (testing "Returns nil when all zeros"
    (let [matrix [[0 1]
                  [0 2]]]
      (is (nil? (find-first-nonzero matrix 0 0))))))

(deftest scale-row-test
  (testing "Scales row to make pivot element equal 1"
    (let [matrix [[2 4 6]
                  [1 2 3]]]
      (is (= [[1 2 3]
              [1 2 3]]
             (scale-row matrix 0 0)))))

  (testing "Handles fractional scaling"
    (let [matrix [[3 6 9]]]
      (is (= [[1 2 3]]
             (scale-row matrix 0 0))))))

(deftest flip-rows-test
  (testing "Swaps two rows"
    (let [matrix [[1 2]
                  [3 4]
                  [5 6]]]
      (is (= [[3 4]
              [1 2]
              [5 6]]
             (flip-rows matrix 0 1)))
      (is (= [[5 6]
              [3 4]
              [1 2]]
             (flip-rows matrix 0 2))))))

(deftest eliminate-row-test
  (testing "Eliminates column in other rows"
    (let [matrix [[1 2 3]
                  [2 4 8]
                  [3 6 9]]]
      (let [result (eliminate-row matrix 0 0)]
        (is (= [1 2 3] (first result)))           ; pivot row unchanged
        (is (= 0 (get-in result [1 0])))          ; eliminated in row 1
        (is (= 0 (get-in result [2 0])))))))      ; eliminated in row 2

(deftest gauss-test
  (testing "Solves simple 2x3 system to RREF"
    (let [matrix [[2 1 5]    ; 2x + y = 5
                  [1 1 3]]   ; x + y = 3
          result (gauss matrix)]
      ; Solution: x = 2, y = 1
      ; In RREF should be: [[1 0 2] [0 1 1]]
      (is (= [[1 0 2] [0 1 1]] result))))

  (testing "Handles system with leading zeros"
    (let [matrix [[0 1 2]    ; y = 2
                  [1 0 3]]   ; x = 3
          result (gauss matrix)]
      ; After row swap and reduction: [[1 0 3] [0 1 2]]
      (is (= [[1 0 3] [0 1 2]] result))))

  (testing "Solves 3x4 system"
    (let [matrix [[1 2 1 8]    ; x + 2y + z = 8
                  [2 1 1 7]    ; 2x + y + z = 7
                  [1 1 2 8]]   ; x + y + 2z = 8
          result (gauss matrix)]
      ; Solution: x = 5/4, y = 9/4, z = 9/4
      (is (= [[1N 0N 0N 5/4] [0N 1N 0N 9/4] [0N 0N 1N 9/4]] result))))

  (testing "Handles all-ones system"
    (let [matrix [[1 1 1 3]
                  [1 1 1 3]
                  [1 1 1 3]]
          result (gauss matrix)]
      ; Should reduce to single equation (dependent system)
      (is (vector? result))
      (is (= 3 (count result)))))

  (testing "Identity augmented matrix"
    (let [matrix [[1 0 0 5]
                  [0 1 0 6]
                  [0 0 1 7]]
          result (gauss matrix)]
      ; Already in RREF, should be unchanged
      (is (= [[1 0 0 5] [0 1 0 6] [0 0 1 7]] result)))))
