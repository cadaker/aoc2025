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

(defn part1 [input]
  (let [all-poses (grid/grid-positions input)
        accessible-poses (filter (fn [p]
                                   (and (roll-at? input p)
                                        (accessible? input p)))
                                 all-poses)]
    (count accessible-poses)))

(defn part2 [input]
  ;; TODO: Implement part 2 when it's revealed
  0)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 4")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))