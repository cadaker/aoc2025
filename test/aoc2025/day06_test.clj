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