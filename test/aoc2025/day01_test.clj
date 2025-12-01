(ns aoc2025.day01-test
  (:require [clojure.test :refer :all]
            [aoc2025.day01 :refer :all]))

(deftest turn-dial-test
  (testing "basic right turn"
    (is (= 60 (turn-dial 50 [:right 10]))))

  (testing "basic left turn"
    (is (= 40 (turn-dial 50 [:left 10]))))

  (testing "right turn wraps around at 99"
    (is (= 5 (turn-dial 95 [:right 10]))))

  (testing "left turn wraps around at 0"
    (is (= 95 (turn-dial 5 [:left 10]))))

  (testing "landing exactly on 0 from right"
    (is (= 0 (turn-dial 90 [:right 10]))))

  (testing "landing exactly on 0 from left"
    (is (= 0 (turn-dial 5 [:left 5]))))

  (testing "staying at 0"
    (is (= 0 (turn-dial 0 [:right 100]))))

  (testing "large right turn with multiple wraps"
    (is (= 50 (turn-dial 50 [:right 200])))))