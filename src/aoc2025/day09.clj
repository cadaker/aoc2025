(ns aoc2025.day09
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day09.txt"))

(defn parse-input [input]
  (mapv (fn [line]
          (mapv parse-long (str/split line #",")))
        (str/split-lines input)))

(defn areas [points]
  (for [[r1 c1] points
        [r2 c2] points]
    (* (abs (+ 1 (- r1 r2)))
       (abs (+ 1 (- c1 c2))))))

(defn part1 [input]
  (apply max (areas input)))

(defn part2 [input]
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 9")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))