(ns aoc2025.day02-test
  (:require [clojure.test :refer :all]
            [aoc2025.day02 :refer :all]))

(deftest digit-count-test
  (testing "single digit"
    (is (= 1 (digit-count 5)))
    (is (= 0 (digit-count 0))) ;; For the purposes of this problem
    (is (= 1 (digit-count 9))))
  (testing "multiple digits"
    (is (= 2 (digit-count 42)))
    (is (= 3 (digit-count 123)))
    (is (= 4 (digit-count 1234)))
    (is (= 10 (digit-count 1188511885)))))

(deftest invalid-n?-test
  (testing "valid repeated sequences"
    (is (invalid-n? 55 1))        ; 5 repeated twice
    (is (invalid-n? 6464 2))      ; 64 repeated twice
    (is (invalid-n? 123123 3))    ; 123 repeated twice
    (is (invalid-n? 99 1))        ; 9 repeated twice
    (is (invalid-n? 1010 2)))     ; 10 repeated twice
  (testing "invalid sequences - wrong n"
    (is (not (invalid-n? 11 2)))  ; 11 is not a 2-digit sequence repeated
    (is (not (invalid-n? 55 2)))) ; 55 is not a 2-digit sequence repeated
  (testing "not repeated"
    (is (not (invalid-n? 123 3))) ; 123 is not repeated
    (is (not (invalid-n? 1234 2)))) ; 12 and 34 are different
  (testing "example numbers from puzzle"
    (is (invalid-n? 11 1))
    (is (invalid-n? 22 1))
    (is (invalid-n? 99 1))
    (is (invalid-n? 1010 2))
    (is (invalid-n? 1188511885 5))
    (is (invalid-n? 222222 3))
    (is (invalid-n? 446446 3))
    (is (invalid-n? 38593859 4))))

(deftest invalid?-test
  (testing "single digit repeated"
    (is (invalid? 11))
    (is (invalid? 22))
    (is (invalid? 55))
    (is (invalid? 99)))
  (testing "multi-digit sequences repeated"
    (is (invalid? 6464))          ; 64 repeated
    (is (invalid? 123123))        ; 123 repeated
    (is (invalid? 1010)))         ; 10 repeated
  (testing "not repeated"
    (is (not (invalid? 123)))     ; single occurrence
    (is (not (invalid? 1234)))    ; no repeated pattern
    (is (not (invalid? 12345))))) ; odd length, can't be repeated twice
  (testing "example invalid IDs from puzzle"
    (is (invalid? 11))
    (is (invalid? 22))
    (is (invalid? 99))
    (is (invalid? 1010))
    (is (invalid? 1188511885))
    (is (invalid? 222222))
    (is (invalid? 446446))
    (is (invalid? 38593859)))
  (testing "should not be invalid"
    (is (not (invalid? 12)))
    (is (not (invalid? 100)))
    (is (not (invalid? 1234))))

(deftest filter-invalid-part1-test
  (testing "small ranges from puzzle examples"
    (is (= [11 22] (filter-invalid-part1 [11 22])))
    (is (= [99] (filter-invalid-part1 [95 115])))
    (is (= [1010] (filter-invalid-part1 [998 1012])))
    (is (= [222222] (filter-invalid-part1 [222220 222224])))
    (is (= [446446] (filter-invalid-part1 [446443 446449]))))
  (testing "single invalid ID in range"
    (is (= [1188511885] (filter-invalid-part1 [1188511880 1188511890])))
    (is (= [38593859] (filter-invalid-part1 [38593856 38593862]))))
  (testing "range with no invalid IDs"
    (is (= [] (filter-invalid-part1 [12 20])))
    (is (= [] (filter-invalid-part1 [100 110]))))
  (testing "range with single element"
    (is (= [11] (filter-invalid-part1 [11 11])))
    (is (= [] (filter-invalid-part1 [12 12])))))