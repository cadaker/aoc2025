(ns aoc2025.day01
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day01.txt"))

(defn parse-input [input]
  (let [lines (str/split-lines input)]
    (map (fn [line]
           (let [direction (first line)
                 distance (parse-long (subs line 1))]
             [(case direction
                \L :left
                \R :right)
              distance]))
         lines)))

(def DIAL-LEN 100)

(defn turn-dial [start [dir amount]]
  (mod (if (= dir :left)
         (- start amount)
         (+ start amount))
       DIAL-LEN))

(def DIAL-START 50)

(defn part1 [input]
  (let [dial-sequence (reductions (partial turn-dial) DIAL-START input)]
    (count (filter {0 :true} dial-sequence))))

(defn part2 [input]
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 1")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))