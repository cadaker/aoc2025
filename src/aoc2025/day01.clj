(ns aoc2025.day01
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day01.txt"))

(defn parse-input [input]
  (str/split-lines input))

(defn part1 [input]
  nil)

(defn part2 [input]
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 1")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))