(ns aoc2025.day01
  (:require [clojure.string :as str] [clojure.math :as math]))

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

(defn signed-amount [[dir amount]]
  (if (= dir :left)
    (- amount)
    amount))

(defn turn-dial [start turn]
  (mod (+ start (signed-amount turn))
       DIAL-LEN))

(def DIAL-START 50)

(defn part1 [input]
  (let [dial-sequence (reductions (partial turn-dial) DIAL-START input)]
    (count (filter {0 :true} dial-sequence))))

(defn zeros-passed [start [dir amount]]
  (if (= dir :right)
    (math/floor-div (+ start amount) DIAL-LEN)
    (zeros-passed (mod (- 100 start) DIAL-LEN)
                  [:right amount])))

(defn count-dial-zeros [start turn]
  [(turn-dial start turn)
   (zeros-passed start turn)])

(defn part2 [input]
  (let [steps (reductions
                 (fn [[start _] turn]
                   (count-dial-zeros start turn))
                 [DIAL-START 0]
                 input)]
    (reduce + (map second steps))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 1")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))