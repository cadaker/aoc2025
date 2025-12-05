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
  (some (partial fresh-single? id) ranges))

(defn part1 [{ranges :ranges ids :ids}]
  (count (filter (partial fresh? ranges) ids)))

(defn part2 [input]
  ;; TODO: Implement part 2
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 5")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))