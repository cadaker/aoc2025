(ns aoc2025.day10
  (:require [clojure.string :as str]
            [aoc2025.gauss :as gauss]))

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

(defn parse-joltage [s]
  (let [joltage-str (second (re-find #"\{([0-9,]+)\}" s))]
    (mapv parse-long (str/split joltage-str #","))))

(defn parse-machine [line]
  {:indicator (parse-indicator line)
   :buttons (parse-buttons line)
   :joltage (parse-joltage line)})

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

(defn build-augmented-matrix [{:keys [buttons joltage]}]
  (let [num-counters (count joltage)
        num-buttons (count buttons)]
    (mapv (fn [counter-idx]
            (let [row (mapv (fn [button-idx]
                              (if (some #{counter-idx} (buttons button-idx))
                                1
                                0))
                            (range num-buttons))]
              (conj row (joltage counter-idx))))
          (range num-counters))))

(defn pivot-column? [matrix column]
  (let [col-values (map #(nth % column) matrix)]
    (and (= 1 (count (filter #(= 1 %) col-values)))
         (every? #{0 1} col-values))))

(defn free-columns [rr-matrix]
  (let [col-count (dec (count (first rr-matrix)))]
    (vec (filter #(not (pivot-column? rr-matrix %)) (range col-count)))))

(defn solve-var [row free-cols free-vals]
  (let [free-coeffs (map #(nth row %) free-cols)]
    (- (last row)
       (reduce + (map * free-vals free-coeffs)))))

(defn solve-system [rr-matrix free-vals]
  (let [col-count (dec (count (first rr-matrix)))
        free-cols (free-columns rr-matrix)]
    (loop [c 0
           r 0
           sol []
           unused-free free-vals]
      (if (>= c col-count)
        sol
        (if (some #(= c %) free-cols)
          (recur (inc c) r (conj sol (first unused-free)) (rest unused-free))
          (recur
            (inc c)
            (inc r)
            (conj sol (solve-var (rr-matrix r) free-cols free-vals))
            unused-free))))))

(defn valid-solution? [vals]
  (every? (fn [x] (and (integer? x) (>= x 0))) vals))

(defn free-variable-max [problem button-no]
  (let [indices ((:buttons problem) button-no)]
    (inc (apply min (map (:joltage problem) indices)))))

(defn all-combinations [xss]
  (loop [xss xss
         output '([])]
    (if (seq xss)
      (recur
        (rest xss)
        (apply concat (map (fn [tuple] (map #(conj tuple %) (first xss))) output)))
      output)))

(defn enumerate-free-options [problem free-cols]
  (let [maxs (map #(free-variable-max problem %) free-cols)]
    (all-combinations (map range maxs))))

(defn solve-problem [problem]
  (let [matrix (gauss/gauss (build-augmented-matrix problem))
        free (free-columns matrix)
        options (enumerate-free-options problem free)
        sols (filter valid-solution? (map #(solve-system matrix %) options))]
    (println problem)
    (gauss/pprint-matrix matrix)
    (apply min (map #(reduce + 0 %) sols))))

(defn part2 [input]
  (reduce + 0 (map solve-problem input)))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 10")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))