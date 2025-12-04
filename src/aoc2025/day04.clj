(ns aoc2025.day04
  (:require [clojure.string :as str]
            [aoc2025.grid :as grid]))

(defn read-input []
  (slurp "resources/day04.txt"))

(defn parse-input [input]
  (grid/grid-from-lines (str/split-lines input)))

(defn roll? [ch]
  (= \@ ch))

(defn roll-at?
  ([grid row col]
   (roll? (grid/grid-get grid row col)))
  ([grid [row col]]
   (roll-at? grid row col)))

(defn count-rolls [grid row col]
  (let [poses (grid/grid-neighbors-8 grid row col)
        items (map (partial grid/grid-getp grid) poses)]
    (count (filter roll? items))))

(defn accessible? [grid [row col]]
  (< (count-rolls grid row col) 4))

(defn all-accessible [grid]
  (let [all-poses (grid/grid-positions grid)
        accessible-poses (filter (fn [p]
                                   (and (roll-at? grid p)
                                        (accessible? grid p)))
                                 all-poses)]
    accessible-poses))

(defn part1 [input]
    (count (all-accessible input)))

(defn remove-accessible [grid]
  (let [acc-rolls (all-accessible grid)]
    [(reduce (fn [grid [row col]]
               (grid/grid-assoc grid row col \.))
             grid
             acc-rolls)
     acc-rolls]))

(defn part2 [input]
  (loop [grid input
         total 0]
    (let [[new-grid removed] (remove-accessible grid)]
      (if (empty? removed)
        total
        (recur new-grid (+ total (count removed)))))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 4")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))