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

(deftest count-dial-zeros-test
  (testing "right turn that doesn't cross 0"
    (is (= [80 0] (count-dial-zeros 50 [:right 30]))))

  (testing "right turn that crosses 0 once"
    (is (= [0 1] (count-dial-zeros 50 [:right 50]))))

  (testing "right turn that crosses 0 once (from near end)"
    (is (= [3 1] (count-dial-zeros 98 [:right 5]))))

  (testing "right turn with 10 crossings"
    (is (= [50 10] (count-dial-zeros 50 [:right 1000]))))

  (testing "right turn with multiple crossings"
    (is (= [0 2] (count-dial-zeros 50 [:right 150]))))

  (testing "left turn that doesn't cross 0"
    (is (= [20 0] (count-dial-zeros 50 [:left 30]))))

  (testing "left turn that crosses 0 once"
    (is (= [90 1] (count-dial-zeros 50 [:left 60]))))

  (testing "left turn landing exactly on 0"
    (is (= [0 1] (count-dial-zeros 5 [:left 5]))))

  (testing "left turn from example (L68 from 50 -> 82)"
    (is (= [82 1] (count-dial-zeros 50 [:left 68]))))

  (testing "left turn with multiple crossings"
    (is (= [90 2] (count-dial-zeros 50 [:left 160]))))

  (testing "starting at 0, right turn"
    (is (= [50 0] (count-dial-zeros 0 [:right 50]))))

  (testing "starting at 0, left turn small"
    (is (= [50 0] (count-dial-zeros 0 [:left 50]))))

  (testing "starting at 0, left turn to wrap once"
    (is (= [0 1] (count-dial-zeros 0 [:left 100])))))