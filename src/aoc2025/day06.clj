(ns aoc2025.day06
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day06.txt"))

(defn parse-input [input]
  (str/split-lines input))

(defn transpose [rows]
  (if (empty? rows)
    []
    (let [cols (map vector (first rows))]
      (reduce
       (fn [cols row]
         (map conj cols row))
       cols
       (rest rows)))))

(defn solve-problem [xs]
  (let [operation (last xs)
        operands (butlast xs)
        nums (map parse-long operands)]
    (cond
      (= operation "*") (reduce * nums)
      (= operation "+") (reduce + nums)
      :else (throw (Exception. "bad operator")))))

(defn part1 [input]
  (let [matrix (map (fn [row] (str/split row #"\s+")) input)
        problems (transpose matrix)]
    (reduce + (map solve-problem problems))))

(defn parse-column [input col]
  (loop [row 0
         data {}]
    (if (>= row (count input))
      data
      (let [ch (nth (nth input row) col)]
        (cond
          (= ch \+) (recur (inc row) (assoc data :op :add))
          (= ch \*) (recur (inc row) (assoc data :op :mul))
          (Character/isDigit ch) (recur
                                   (inc row)
                                   (update data :val (fn [prev]
                                                       (let [digit (- (int ch) (int \0))]
                                                         (+ (* 10 (or prev 0)) digit)))))
          :else (recur (inc row) data))))))

(defn update-problem [problem col-data]
  (let [with-val (if (:val col-data)
                   (update problem :vals (fn [vals]
                                           (conj (or vals []) (:val col-data))))
                   problem)
        with-op (if (:op col-data)
                  (assoc with-val :op (:op col-data))
                  with-val)]
    with-op))

(defn parse-problems [input]
  (let [max-cols (count (first input))]
    (loop [col 0
           cur-problem {}
           problems []]
      (if (>= col max-cols)
        (conj problems cur-problem)
        (let [data (parse-column input col)]
          (if (empty? data)
            (recur
              (inc col)
              {}
              (conj problems cur-problem))
            (recur
              (inc col)
              (update-problem cur-problem data)
              problems)))))))

(defn eval-problem [{vals :vals, op :op}]
  (cond
    (= op :add) (reduce + vals)
    (= op :mul) (reduce * vals)
    :else (throw (Exception. "bad op"))))

(defn part2 [input]
  (reduce +
          (map eval-problem (parse-problems input))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 6")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))