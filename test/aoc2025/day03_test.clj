(ns aoc2025.day03-test
  (:require [clojure.test :refer :all]
            [aoc2025.day03 :refer :all]))

(deftest parse-input-test
  (testing "single line"
    (is (= [[9 8 7 6 5 4 3 2 1]]
           (parse-input "987654321"))))
  (testing "multiple lines"
    (is (= [[9 8 7 6 5 4 3 2 1 1 1 1 1 1 1]
            [8 1 1 1 1 1 1 1 1 1 1 1 1 1 9]
            [2 3 4 2 3 4 2 3 4 2 3 4 2 7 8]
            [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1]]
           (parse-input "987654321111111\n811111111111119\n234234234234278\n818181911112111")))))

(deftest find-max-test
  (testing "single element"
    (is (= [5 0] (find-max [5]))))
  (testing "max at beginning"
    (is (= [9 0] (find-max [9 8 7 6 5 4 3 2 1]))))
  (testing "max at end"
    (is (= [9 4] (find-max [1 2 3 4 9]))))
  (testing "max in middle"
    (is (= [9 2] (find-max [1 2 9 3 4]))))
  (testing "multiple equal maxima - returns first"
    (is (= [9 1] (find-max [1 9 5 9 3]))))
  (testing "all equal values"
    (is (= [5 0] (find-max [5 5 5 5 5])))))

(deftest joltage-test
  (testing "adjacent batteries at start"
    (is (= 98 (joltage [9 8 7 6 5 4 3 2 1 1 1 1 1 1 1]))))
  (testing "batteries at opposite ends"
    (is (= 89 (joltage [8 1 1 1 1 1 1 1 1 1 1 1 1 1 9]))))
  (testing "adjacent batteries at end"
    (is (= 78 (joltage [2 3 4 2 3 4 2 3 4 2 3 4 2 7 8]))))
  (testing "non-adjacent batteries"
    (is (= 92 (joltage [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1]))))
  (testing "simple cases"
    (is (= 99 (joltage [9 9])))
    (is (= 98 (joltage [9 8])))
    (is (= 91 (joltage [9 1 1 1])))
    (is (= 54 (joltage [5 4 3 2 1])))))

(deftest joltage-12-test
  (testing "skip 1s at end"
    (is (= 987654321111 (joltage-12 [9 8 7 6 5 4 3 2 1 1 1 1 1 1 1]))))
  (testing "skip 1s in middle"
    (is (= 811111111119 (joltage-12 [8 1 1 1 1 1 1 1 1 1 1 1 1 1 9]))))
  (testing "skip smaller digits near start"
    (is (= 434234234278 (joltage-12 [2 3 4 2 3 4 2 3 4 2 3 4 2 7 8]))))
  (testing "skip 1s near front"
    (is (= 888911112111 (joltage-12 [8 1 8 1 8 1 9 1 1 1 1 2 1 1 1]))))
  (testing "select all digits when exactly 12"
    (is (= 123456789012 (joltage-12 [1 2 3 4 5 6 7 8 9 0 1 2]))))
  (testing "simple greedy selection"
    (is (= 999999999999 (joltage-12 [9 9 9 9 9 9 9 9 9 9 9 9 1 1 1])))))