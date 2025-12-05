(ns aoc2025.day05-test
  (:require [clojure.test :refer :all]
            [aoc2025.day05 :refer :all]))

(deftest fresh-single?-test
  (testing "ID within range"
    (is (= true (fresh-single? 5 [3 7]))))

  (testing "ID at start of range"
    (is (= true (fresh-single? 3 [3 7]))))

  (testing "ID at end of range"
    (is (= true (fresh-single? 7 [3 7]))))

  (testing "ID before range"
    (is (= false (fresh-single? 2 [3 7]))))

  (testing "ID after range"
    (is (= false (fresh-single? 8 [3 7])))))

(deftest fresh?-test
  (testing "ID in first range"
    (is (= true (fresh? [[3 5] [10 14]] 4))))

  (testing "ID in second range"
    (is (= true (fresh? [[3 5] [10 14]] 12))))

  (testing "ID not in any range"
    (is (= false (fresh? [[3 5] [10 14]] 7))))

  (testing "ID at boundary"
    (is (= true (fresh? [[3 5] [10 14]] 5)))))

(deftest merge-ranges-test
  (testing "add range to empty set"
    (is (= [[5 10]] (merge-ranges [] [5 10]))))

  (testing "add non-overlapping range before existing"
    (is (= [[1 3] [10 15]] (merge-ranges [[10 15]] [1 3]))))

  (testing "add non-overlapping range after existing"
    (is (= [[1 3] [10 15]] (merge-ranges [[1 3]] [10 15]))))

  (testing "add range that overlaps at start"
    (is (= [[5 15]] (merge-ranges [[10 15]] [5 12]))))

  (testing "add range that overlaps at end"
    (is (= [[10 20]] (merge-ranges [[10 15]] [13 20]))))

  (testing "add range completely contained in existing"
    (is (= [[10 20]] (merge-ranges [[10 20]] [12 15]))))

  (testing "add range that completely contains existing"
    (is (= [[5 25]] (merge-ranges [[10 20]] [5 25]))))

  (testing "add range that merges multiple existing ranges"
    (is (= [[5 20]] (merge-ranges [[5 10] [15 20]] [8 18]))))

  (testing "add range that merges all existing ranges"
    (is (= [[1 30]] (merge-ranges [[5 10] [15 20] [25 28]] [1 30]))))

  (testing "add adjacent range before (touching)"
    (is (= [[5 15]] (merge-ranges [[10 15]] [5 9]))))

  (testing "add adjacent range after (touching)"
    (is (= [[10 20]] (merge-ranges [[10 15]] [16 20]))))

  (testing "add range between two existing ranges, merging all three"
    (is (= [[1 20]] (merge-ranges [[1 5] [15 20]] [4 16]))))

  (testing "add range with multiple non-overlapping existing"
    (is (= [[1 5] [10 15] [20 25]] (merge-ranges [[1 5] [20 25]] [10 15]))))

  (testing "example scenario building up overlapping ranges"
    (is (= [[3 20]] (-> []
                        (merge-ranges [3 5])
                        (merge-ranges [10 14])
                        (merge-ranges [16 20])
                        (merge-ranges [12 18])
                        (merge-ranges [6 9]))))))

(deftest part2-test
  (testing "example from problem description"
    (let [input {:ranges [[3 5] [10 14] [16 20] [12 18]]
                 :ids []}]
      (is (= 14 (part2 input)))))

  (testing "single range"
    (let [input {:ranges [[5 10]]
                 :ids []}]
      (is (= 6 (part2 input)))))

  (testing "non-overlapping ranges"
    (let [input {:ranges [[1 3] [5 7] [9 10]]
                 :ids []}]
      (is (= 8 (part2 input)))))

  (testing "completely overlapping ranges"
    (let [input {:ranges [[1 10] [3 8]]
                 :ids []}]
      (is (= 10 (part2 input)))))

  (testing "adjacent ranges (not overlapping)"
    (let [input {:ranges [[1 5] [6 10]]
                 :ids []}]
      (is (= 10 (part2 input))))))