(ns aoc2025.day06
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day06.txt"))

(defn parse-input [input]
  (str/split-lines input))

(defn transpose [rows]
  (if (empty? rows)
    []
    (let [cols (map vector (first rows))]
      (reduce
       (fn [cols row]
         (map conj cols row))
       cols
       (rest rows)))))

(defn solve-problem [xs]
  (let [operation (last xs)
        operands (butlast xs)
        nums (map parse-long operands)]
    (cond
      (= operation "*") (reduce * nums)
      (= operation "+") (reduce + nums)
      :else (throw (Exception. "bad operator")))))

(defn part1 [input]
  (let [matrix (map (fn [row] (str/split row #"\s+")) input)
        problems (transpose matrix)]
    (reduce + (map solve-problem problems))))

(defn part2 [input]
  ;; TODO: Implement part 2
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 6")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))