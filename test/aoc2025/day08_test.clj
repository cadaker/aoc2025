(ns aoc2025.day08-test
  (:require [clojure.test :refer :all]
            [aoc2025.day08 :refer :all]
            [aoc2025.union_find :as uf]))

(def example-input
  "162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689")

(deftest test-parse-input
  (testing "Parses coordinate strings into vectors of numbers"
    (let [parsed (parse-input example-input)]
      (is (= 20 (count parsed)))
      (is (= [162 817 812] (first parsed)))
      (is (= [425 690 689] (last parsed)))
      (is (every? #(= 3 (count %)) parsed)))))

(deftest test-squared-distance
  (testing "Calculates squared Euclidean distance correctly"
    (is (= 0 (squared-distance [0 0 0] [0 0 0])))
    (is (= 3 (squared-distance [0 0 0] [1 1 1])))
    (is (= 6 (squared-distance [1 2 3] [3 3 2])))
    (is (= 200 (squared-distance [0 0 0] [10 10 0])))))

(deftest test-all-distances
  (testing "Generates all pairs with squared distances, sorted"
    (let [points [[0 0 0] [1 0 0] [0 1 0]]
          dists (all-distances points)]
      (is (= 3 (count dists)))
      (is (= 1 (first (first dists))))
      (is (= [1 0 1] (first dists)))
      (is (= [1 0 2] (second dists)))
      (is (= [2 1 2] (nth dists 2)))
      (is (apply <= (map first dists))))))

(deftest test-all-distances-ordering
  (testing "All-distances returns pairs in sorted order by distance"
    (let [points [[0 0 0] [10 0 0] [1 0 0]]
          dists (all-distances points)]
      (is (= 1 (first (first dists))))
      (is (= 81 (first (second dists))))
      (is (= 100 (first (nth dists 2)))))))

(deftest test-count-groups-single-component
  (testing "Count groups with all elements in one component"
    (let [conn (-> (uf/make-union-find [0 1 2 3])
                   (uf/join 0 1)
                   (uf/join 1 2)
                   (uf/join 2 3))
          groups (count-groups conn)]
      (is (= [4] groups)))))

(deftest test-count-groups-multiple-components
  (testing "Count groups with multiple separate components"
    (let [conn (-> (uf/make-union-find [0 1 2 3 4])
                   (uf/join 0 1)
                   (uf/join 2 3))
          groups (count-groups conn)]
      (is (= 3 (count groups)))
      (is (= [2 2 1] groups)))))

(deftest test-connect-all
  (testing "Connect-all joins multiple pairs"
    (let [conn (uf/make-union-find [0 1 2 3])
          dists [[1 0 1] [2 1 2] [3 2 3]]
          conn' (connect-all conn dists)
          groups (count-groups conn')]
      (is (= [4] groups)))))

(deftest test-connect-all-partial
  (testing "Connect-all with subset of distances"
    (let [conn (uf/make-union-find [0 1 2 3 4])
          dists [[1 0 1] [2 2 3]]
          conn' (connect-all conn dists)
          groups (count-groups conn')]
      (is (= [2 2 1] groups)))))

(deftest test-connected-groups-example
  (testing "Connected-groups with example data and 10 connections"
    (let [parsed (parse-input example-input)
          groups (connected-groups parsed 10)]
      (is (= [5 4 2] (take 3 groups)))
      (is (= 40 (reduce * (take 3 groups)))))))

(deftest test-connected-groups-small
  (testing "Connected-groups with simple input"
    (let [points [[0 0 0] [1 0 0] [10 0 0]]
          groups (connected-groups points 1)]
      (is (= [2 1] groups)))))

(deftest test-connect
  (testing "Connect function joins elements in union-find"
    (let [conn (uf/make-union-find [0 1 2])
          conn' (connect conn [100 0 1])
          [_ root0] (uf/lookup conn' 0)
          [_ root1] (uf/lookup conn' 1)]
      (is (= root0 root1)))))