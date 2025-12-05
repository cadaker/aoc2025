(ns aoc2025.day05
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day05.txt"))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        [range-lines _ id-lines] (partition-by str/blank? lines)
        ranges (map (fn [line]
                      (let [[start end] (str/split line #"-")]
                        [(parse-long start) (parse-long end)]))
                    range-lines)
        ids (map parse-long id-lines)]
    {:ranges ranges
     :ids ids}))

(defn fresh-single? [id [start end]]
  (<= start id end))

(defn fresh? [ranges id]
  (boolean (some (partial fresh-single? id) ranges)))

(defn part1 [{ranges :ranges ids :ids}]
  (count (filter (partial fresh? ranges) ids)))

(defn overlaps? [[s1 e1] [s2 e2]]
  (or (<= s1 s2 e1)
      (<= s1 e2 e1)
      (<= s2 s1 e2)
      (<= s2 e1 e2)
      (= (+ 1 e1) s2)
      (= (+ 1 e2) s1)))

(defn merge-overlapping [[s1 e1] [s2 e2]]
  [(min s1 s2) (max e1 e2)])

(defn merge-ranges [ranges range]
  (loop [input ranges
         output []
         current range]
    (cond
      (empty? input) (conj output current)
      (overlaps? (first input) current) (recur
                                          (rest input)
                                          output
                                          (merge-overlapping (first input) current))
      (< (first current) (first (first input))) (vec (concat output [current] input))
      :else (recur
              (rest input)
              (conj output (first input))
              current))))

(defn range-size [[start end]]
  (+ end 1 (- start)))

(defn part2 [{ranges :ranges}]
  (let [merged-ranges (reduce merge-ranges [] ranges)]
    (reduce + 0 (map range-size merged-ranges))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 5")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))