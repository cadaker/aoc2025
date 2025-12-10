(ns aoc2025.gauss
  (:require [clojure.string :as str]))

(defn find-first-nonzero [matrix start-row column]
  (let [indexed-column (map-indexed
                        (fn [ix row]
                          [(+ ix start-row) (nth row column)])
                        (drop start-row matrix))]
    (first (first (filter #(not (zero? (second %))) indexed-column)))))

(defn scale-row [matrix row column]
  (let [factor (get-in matrix [row column])]
    (update matrix row (partial mapv #(/ % factor)))))

(defn flip-rows [matrix r1 r2]
  (let [row1 (matrix r1)
        row2 (matrix r2)]
    (assoc (assoc matrix r1 row2) r2 row1)))

(defn eliminate-row [matrix row column]
  (let [el-row (matrix row)
        el-factor (el-row column)]
    (vec (map-indexed
          (fn [rowno cur-row]
            (if (= rowno row)
              cur-row
              (let [factor (/ (cur-row column) el-factor)]
                (mapv (fn [x el-x] (- x (* el-x factor))) cur-row el-row))))
          matrix))))

(defn gauss [matrix]
  (let [nrows (count matrix)
        ncols (count (first matrix))]
    (loop [matrix matrix
           rows-reduced 0
           column 0]
      (if (>= column ncols)
        matrix
        (let [r (find-first-nonzero matrix rows-reduced column)]
          (cond
            (nil? r) (recur matrix rows-reduced (inc column))
            (not= 1 (get-in matrix [r column])) (recur (scale-row matrix r column) rows-reduced column)
            (not= r rows-reduced) (recur (flip-rows matrix r rows-reduced) rows-reduced column)
            :else (recur (eliminate-row matrix r column) (inc rows-reduced) (inc column))))))))

(defn format-number [n]
  (cond
    (ratio? n) (str (numerator n) "/" (denominator n))
    (integer? n) (str n)
    :else (str n)))

(defn pad-left [s width]
  (let [padding (- width (count s))]
    (if (pos? padding)
      (str (apply str (repeat padding " ")) s)
      s)))

(defn pprint-matrix
  "Pretty-prints a matrix with aligned columns.
   Optionally shows a separator before the last column (for augmented matrices)."
  ([matrix] (pprint-matrix matrix false))
  ([matrix show-separator]
   (let [ncols (count (first matrix))
         sep-col (when show-separator (dec ncols))
         formatted (mapv (fn [row] (mapv format-number row)) matrix)
         col-widths (mapv (fn [col-idx]
                            (apply max (map #(count (nth % col-idx)) formatted)))
                          (range ncols))]
     (doseq [row formatted]
       (doseq [col-idx (range ncols)]
         (when (and show-separator (= col-idx sep-col))
           (print "| "))
         (print (pad-left (nth row col-idx) (nth col-widths col-idx)))
         (when (< col-idx (dec ncols))
           (print " ")))
       (println)))))
