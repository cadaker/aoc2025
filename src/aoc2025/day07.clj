(ns aoc2025.day07
  (:require [clojure.string :as str]
            clojure.set))

(defn read-input []
  (slurp "resources/day07.txt"))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        start (first (for [[r row] (map-indexed vector lines)
                           [c ch] (map-indexed vector row)
                           :when (= ch \S)]
                       [r c]))
        splitters (mapv (fn [row]
                          (set (keep-indexed (fn [c ch]
                                               (when (= ch \^) c))
                                             row)))
                        lines)]
    {:start start
     :splitters splitters}))

(defn propagate-beams [beams splitters]
  (reduce
   (fn [output beam]
     (if (splitters beam)
       (conj output (dec beam) (inc beam))
       (conj output beam)))
   #{}
   beams))

(defn count-splits [[r c] all-splitters]
  (:splits (reduce
            (fn [{beams :beams, splits :splits} splitters]
              {:beams (propagate-beams beams splitters)
               :splits (+ splits (count (clojure.set/intersection beams splitters)))})
            {:beams #{c}, :splits 0}
            (drop r all-splitters))))

(defn part1 [input]
  (count-splits (:start input) (:splitters input)))

(defn part2 [input]
  ;; Part 2 solution (not yet revealed)
  nil)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 7")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))