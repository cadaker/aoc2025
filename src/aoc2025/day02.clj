(ns aoc2025.day02
  (:require [clojure.string :as str] [aoc2025.utils :as utils]))

(defn read-input []
  (slurp "resources/day02.txt"))

(defn parse-input [input]
  (let [ranges (str/split (str/trim input) #",")]
    (map (fn [range-str]
           (let [[start end] (str/split range-str #"-")]
             [(parse-long start) (parse-long end)]))
         ranges)))

(defn digit-count [n]
  (loop [n n, count 0]
    (if (zero? n)
      count
      (recur (quot n 10) (inc count)))))

(defn invalid-n? [id n]
  (let [remainder (mod (digit-count id) n)
        group-count (quot (digit-count id) n)
        pow10 (utils/pow 10 n)
        group (mod id pow10)
        non-group (/ (- (utils/pow 10 (* n group-count)) 1) (- (utils/pow 10 n) 1))]
    (and (zero? remainder)
         (not= group-count 1)
         (= id (* non-group group)))))

(defn invalid? [id]
  (let [count (digit-count id)
        max (quot count 2)
        groups-to-try (range 1 (inc max))]
    (some (fn [n] (invalid-n? id n)) groups-to-try)))

(defn invalid-part1? [id]
  (let [count (digit-count id)
        n (quot count 2)]
    (and (even? count)
         (invalid-n? id n))))

(defn filter-invalid-part1 [[start end]]
  (filter invalid-part1? (range start (inc end))))

(defn part1 [input]
  (reduce + 0 (mapcat filter-invalid-part1 input)))

(defn filter-invalid [[start end]]
  (filter invalid? (range start (inc end))))

(defn part2 [input]
  (reduce + 0 (mapcat filter-invalid input)))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 2")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))