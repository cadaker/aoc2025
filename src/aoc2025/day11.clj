(ns aoc2025.day11
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day11.txt"))

(defn parse-line [line]
  (let [[device outputs-str] (str/split line #": ")]
    [device (set (str/split outputs-str #" "))]))

(defn parse-input [input]
  (let [lines (str/split-lines input)]
    (into {} (map parse-line lines))))

(def START "you")
(def END "out")

(defn count-paths [graph start end]
  (loop [stack [start]
         paths {end 1}]
    (if (empty? stack)
      paths
      (let [top (peek stack)
            children (graph top)]
        (if (every? paths children)
          (recur (pop stack) (assoc paths top (reduce + 0 (map paths children))))
          (recur (apply conj stack children) paths))))))

(defn part1 [input]
  ((count-paths input START END) START))

(defn part2 [input]
  ;; TODO: Implement part 2
  0)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 11")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))
