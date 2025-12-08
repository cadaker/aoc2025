(ns aoc2025.day08
  (:require [clojure.string :as str] [aoc2025.union_find :as uf]))

(defn read-input []
  (slurp "resources/day08.txt"))

(defn parse-input [input]
  ;; Parse junction box coordinates (X,Y,Z)
  ;; Returns a collection of [x y z] coordinates
  (mapv (fn [line]
          (mapv parse-long (str/split line #",")))
        (str/split-lines input)))

(defn squared-distance [[x1 y1 z1] [x2 y2 z2]]
  (+ (* (- x2 x1) (- x2 x1))
     (* (- y2 y1) (- y2 y1))
     (* (- z2 z1) (- z2 z1))))

(defn all-distances [points]
  (let [indexed-points (map-indexed vector points)
        pairs (for [[i p1] indexed-points
                    [j p2] indexed-points
                    :when (< i j)]
                [(squared-distance p1 p2) i j])]
    (sort-by first pairs)))

(defn connect [conn [dist2 x y]]
  (uf/join conn x y))

(defn count-groups [conn]
  (let [all-groups (second (reduce
                            (fn [[conn groups] x]
                              (let [[conn' group] (uf/lookup conn x)]
                                [conn' (conj groups group)]))
                            [conn []]
                            (keys conn)))
        counts-of-groups (reduce
                          (fn [counts g]
                            (update counts g (fn [x] (inc (or x 0)))))
                          {}
                          all-groups)]
    (sort > (vals counts-of-groups))))

(defn connect-all [conns dists]
  (reduce
   connect
   conns
   dists))

(defn connected-groups [input n]
  (let [dists (all-distances input)
        indexes (map first (map-indexed vector input))
        conns (connect-all (uf/make-union-find indexes) (take n dists))]
    (count-groups conns)))

(defn part1 [input]
  (let [groups (connected-groups input 1000)]
    (reduce * (take 3 groups))))

(defn try-join [conns x y]
  (let [[conns' gx] (uf/lookup conns x)
        [conns'' gy] (uf/lookup conns y)]
    [(uf/join conns'' x y) (not= gx gy)]))

(defn connecting-wires [conns dists]
  (lazy-seq
    (when-let [[[dist2 x y] & rest-dists] (seq dists)]
      (let [[conns' was-chosen] (try-join conns x y)]
        (if was-chosen
          (cons [x y] (connecting-wires conns' rest-dists))
          (connecting-wires conns' rest-dists))))))

(defn get-connecting-wires [input]
  (let [input-count (count input)
        dists (all-distances input)
        indexes (map first (map-indexed vector input))]
    (take
     (dec input-count)
     (connecting-wires (uf/make-union-find indexes) dists))))

(defn part2 [input]
  (let [wires (get-connecting-wires input)
        [index1 index2] (last wires)]
    (* (first (nth input index1))
       (first (nth input index2)))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 8")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))