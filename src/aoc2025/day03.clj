(ns aoc2025.day03
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day03.txt"))

(defn parse-input [input]
  (let [lines (str/split-lines (str/trim input))]
    (map (fn [line]
           (map #(- (int %) (int \0)) line))
         lines)))

(defn find-max [xs]
  (let [indexed-xs (map vector xs (iterate inc 0))]
    (reduce (fn [[max-x max-ix] [x ix]]
              (if (> x max-x)
                [x ix]
                [max-x max-ix]))
            [(first xs) 0]
            indexed-xs)))

(defn joltage [xs]
  (let [[first-digit ix] (find-max (drop-last xs))
        [second-digit _] (find-max (nthnext xs (inc ix)))]
    (+ (* 10 first-digit) second-digit)))

(defn part1 [input]
  (reduce + (map joltage input)))

(defn part2 [input]
  ;; TODO: Implement part 2
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 3")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))