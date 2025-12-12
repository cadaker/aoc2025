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

(def part2-example
  "svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out")

(deftest part2-test
  (testing "Counts total paths from svr to out"
    (let [graph (parse-input part2-example)]
      (is (= 8 ((count-paths graph "svr" "out") "svr")))))

  (testing "Counts paths visiting both dac and fft"
    (let [graph (parse-input part2-example)]
      (is (= 2 (part2 graph))))))
