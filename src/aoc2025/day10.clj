(ns aoc2025.day10
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day10.txt"))

(defn parse-indicator [s]
  (let [indicator (second (re-find #"\[([.#]+)\]" s))]
    (vec indicator)))

(defn parse-buttons [s]
  (let [button-strs (re-seq #"\(([0-9,]+)\)" s)]
    (mapv (fn [[_ nums]]
            (if (str/includes? nums ",")
              (mapv parse-long (str/split nums #","))
              [(parse-long nums)]))
          button-strs)))

(defn parse-machine [line]
  {:indicator (parse-indicator line)
   :buttons (parse-buttons line)})

(defn parse-input [input]
  (let [lines (str/split-lines input)]
    (mapv parse-machine lines)))

(defn indicator->bitfield [indicator]
  (reduce (fn [acc char]
            (bit-or (bit-shift-left acc 1)
                    (if (= char \#) 1 0)))
          0
          (reverse indicator)))

(defn button->bitfield [button-indices]
  (reduce (fn [acc idx]
            (bit-set acc idx))
          0
          button-indices))

(defn permutations [xs]
  (lazy-seq
    (if (seq xs)
      (let [tail (permutations (rest xs))]
        (concat tail
                (map #(cons (first xs) %) tail)))
      '(()))))

(defn configured? [indicator buttons]
  (= (indicator->bitfield indicator)
     (reduce bit-xor 0 (map button->bitfield buttons))))

(defn min-presses [{buttons :buttons, indicator :indicator}]
  (let [valid-combos (filter (partial configured? indicator) (permutations buttons))]
    (apply min (map count valid-combos))))

(defn part1 [input]
  (reduce + 0 (map min-presses input)))

(defn part2 [input]
  ;; TODO: Implement part 2
  0)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 10")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))