(ns aoc2025.day09-test
  (:require [clojure.test :refer :all]
            [aoc2025.day09 :refer :all]))

(def example-input
  "7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3")

(deftest test-parse-input
  (testing "Parses coordinate strings into vectors of numbers"
    (let [parsed (parse-input example-input)]
      (is (= 8 (count parsed)))
      (is (= [7 1] (first parsed)))
      (is (= [7 3] (last parsed)))
      (is (every? #(= 2 (count %)) parsed)))))

(deftest test-example
  (testing "Example case: largest rectangle has area 50"
    (let [parsed (parse-input example-input)]
      (is (= 50 (part1 parsed))))))