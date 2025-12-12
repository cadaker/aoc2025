(ns aoc2025.day12
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day12.txt"))

(defn parse-shape [lines]
  (mapv vec lines))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        [shape-lines region-lines] (split-with #(not (re-matches #"\d+x\d+:.*" %)) lines)
        shape-groups (partition 4 (filter #(not (str/blank? %)) shape-lines))
        shapes (into {}
                 (map (fn [[header & grid-lines]]
                        (let [id (parse-long (subs header 0 1))]
                          [id (parse-shape grid-lines)]))
                      shape-groups))
        regions (mapv (fn [line]
                       (let [[dims quantities] (str/split line #":\s*")
                             [w h] (mapv parse-long (str/split dims #"x"))
                             qtys (mapv parse-long (str/split quantities #"\s+"))]
                         {:width w
                          :height h
                          :quantities qtys}))
                     region-lines)]
    {:shapes shapes
     :regions regions}))

(defn shape-size [shape]
  (count (filter #{\#} (apply concat shape))))

(defn problem-bounds [shape-sizes {:keys [:width :height :quantities]}]
  {
    :size (* width height)
    :ub (* 9 (reduce + 0 quantities))
    :lb (reduce + 0 (map-indexed (fn [i q] (* q (shape-sizes i))) quantities))
    })

(defn classify [{:keys [:size :ub :lb]}]
  (cond
    (>= size ub) :yes
    (< size lb) :no
    :else :maybe))

(defn part1 [{:keys [:shapes :regions]}]
  (let [shape-sizes (into {} (map (fn [[k v]] [k (shape-size v)]) shapes))
        bounds (map (partial problem-bounds shape-sizes) regions)
        classifications (map classify bounds)]
    (assert (not (contains? (set classifications) :maybe)))
    (count (filter #{:yes} classifications))))

(defn part2 [input]
  ;; TODO: Implement part 2 when it becomes available
  0)

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 12")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))
