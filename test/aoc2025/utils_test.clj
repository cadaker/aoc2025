(ns aoc2025.utils-test
  (:require [clojure.test :refer :all]
            [aoc2025.utils :refer :all]))

(deftest pow-test
  (testing "base cases"
    (is (= 1 (pow 10 0)))
    (is (= 10 (pow 10 1)))
    (is (= 1 (pow 5 0))))
  (testing "small exponents"
    (is (= 100 (pow 10 2)))
    (is (= 1000 (pow 10 3)))
    (is (= 10000 (pow 10 4))))
  (testing "larger exponents"
    (is (= 1000000 (pow 10 6)))
    (is (= 1000000000 (pow 10 9))))
  (testing "other bases"
    (is (= 8 (pow 2 3)))
    (is (= 27 (pow 3 3)))
    (is (= 625 (pow 5 4)))))