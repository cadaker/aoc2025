(ns aoc2025.day06-test
  (:require [clojure.test :refer :all]
            [aoc2025.day06 :refer :all]))

(deftest transpose-test
  (testing "2x2 matrix"
    (is (= [[:a :c]
            [:b :d]]
           (transpose [[:a :b]
                       [:c :d]]))))

  (testing "3x3 matrix"
    (is (= [[1 4 7]
            [2 5 8]
            [3 6 9]]
           (transpose [[1 2 3]
                       [4 5 6]
                       [7 8 9]]))))

  (testing "2x3 matrix (2 rows, 3 columns)"
    (is (= [[1 4]
            [2 5]
            [3 6]]
           (transpose [[1 2 3]
                       [4 5 6]]))))

  (testing "3x2 matrix (3 rows, 2 columns)"
    (is (= [[1 3 5]
            [2 4 6]]
           (transpose [[1 2]
                       [3 4]
                       [5 6]]))))

  (testing "single row"
    (is (= [[1] [2] [3]]
           (transpose [[1 2 3]]))))

  (testing "single column"
    (is (= [[1 2 3]]
           (transpose [[1] [2] [3]]))))

  (testing "1x1 matrix"
    (is (= [[42]]
           (transpose [[42]]))))

  (testing "empty matrix"
    (is (= []
           (transpose [])))))

(deftest parse-column-test
  (testing "column with only addition operator"
    (is (= {:op :add}
           (parse-column ["   "
                          " + "
                          "   "] 1))))

  (testing "column with only multiplication operator"
    (is (= {:op :mul}
           (parse-column ["   "
                          " * "
                          "   "] 1))))

  (testing "column with single digit"
    (is (= {:val 7}
           (parse-column ["   "
                          " 7 "
                          "   "] 1))))

  (testing "column with two-digit number"
    (is (= {:val 42}
           (parse-column ["   "
                          " 4 "
                          " 2 "
                          "   "] 1))))

  (testing "column with three-digit number"
    (is (= {:val 123}
           (parse-column [" 1 "
                          " 2 "
                          " 3 "
                          "   "] 1))))

  (testing "column with number and operator"
    (is (= {:val 123 :op :mul}
           (parse-column [" 1 "
                          " 2 "
                          " 3 "
                          " * "] 1))))

  (testing "column with number and add operator"
    (is (= {:val 45 :op :add}
           (parse-column [" 4 "
                          " 5 "
                          " + "] 1))))

  (testing "column with all spaces"
    (is (= {}
           (parse-column ["   "
                          "   "
                          "   "] 1))))

  (testing "column at index 0"
    (is (= {:val 9 :op :add}
           (parse-column ["9  "
                          "+  "
                          "   "] 0))))

  (testing "column at different index"
    (is (= {:val 56}
           (parse-column ["  5"
                          "  6"
                          "   "] 2)))))

(deftest parse-problems-test
  (testing "single problem with one number and multiplication"
    (is (= [{:vals [123] :op :mul}]
           (parse-problems ["1"
                            "2"
                            "3"
                            "*"]))))

  (testing "single problem with two numbers and addition"
    (is (= [{:vals [12 34] :op :add}]
           (parse-problems ["13"
                            "24"
                            "+ "]))))

  (testing "two problems separated by space"
    (is (= [{:vals [123] :op :mul}
            {:vals [45] :op :add}]
           (parse-problems ["1 4"
                            "2 5"
                            "3  "
                            "* +"]))))

  (testing "problem with three numbers"
    (is (= [{:vals [123 45 6] :op :mul}]
           (parse-problems ["146"
                            "25 "
                            "3  "
                            "*  "]))))

  (testing "multiple problems with different operations"
    (is (= [{:vals [123 45 6] :op :mul}
            {:vals [328 64 98] :op :add}]
           (parse-problems ["146 369"
                            "25  248"
                            "3   8  "
                            "*   +  "])))))