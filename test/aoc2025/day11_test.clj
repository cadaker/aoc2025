(ns aoc2025.day11-test
  (:require [clojure.test :refer :all]
            [aoc2025.day11 :refer :all]))

(deftest parse-input-test
  (testing "Parses device connections into map of sets"
    (let [input "you: bbb ccc
bbb: ddd eee
ccc: fff
ddd: out
eee: out
fff: ggg"
          result (parse-input input)]
      (is (= {"you" #{"bbb" "ccc"}
              "bbb" #{"ddd" "eee"}
              "ccc" #{"fff"}
              "ddd" #{"out"}
              "eee" #{"out"}
              "fff" #{"ggg"}}
             result)))))
