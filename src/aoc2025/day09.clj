(ns aoc2025.day09
  (:require [clojure.string :as str]))

(defn read-input []
  (slurp "resources/day09.txt"))

(defn parse-input [input]
  (mapv (fn [line]
          (mapv parse-long (str/split line #",")))
        (str/split-lines input)))

(defn rectangle-area [[x1 y1] [x2 y2]]
  (* (+ 1 (abs (- x1 x2)))
     (+ 1 (abs (- y1 y2)))))

(defn areas [points]
  (for [p1 points
        p2 points]
    (rectangle-area p1 p2)))

(defn part1 [input]
  (apply max (areas input)))

(defn segment-direction [[x1 y1] [x2 y2]]
  (cond
    (= y1 y2) (if (< x1 x2) :east :west)
    (= x1 x2) (if (< y1 y2) :south :north)
    :else (throw (ex-info "Points not aligned" {:p1 [x1 y1] :p2 [x2 y2]}))))

(defn signed-area [points]
  "Calculate signed area of polygon using shoelace formula.
   In left-handed (x right, y down) coordinate system:
   - Positive area means clockwise
   - Negative area means counterclockwise"
  (let [n (count points)]
    (/ (reduce +
               (for [i (range n)]
                 (let [[x1 y1] (nth points i)
                       [x2 y2] (nth points (mod (inc i) n))]
                   (- (* x1 y2) (* x2 y1)))))
       2)))

;; Convert input coordinates to contour representation
(defn input->contour [points]
  "Convert a sequence of [x y] points into contour representation.
   The contour winds around the outside of the squares defined by the points."
  (let [;; Ensure counterclockwise order (negative signed area in left-handed system)
        area (signed-area points)
        oriented-points (if (pos? area)
                          (vec (reverse points))
                          points)
        n (count oriented-points)
        ;; Build segments connecting consecutive points
        segments (for [i (range n)]
                   (let [p1 (nth oriented-points i)
                         p2 (nth oriented-points (mod (inc i) n))]
                     [p1 p2]))

        ;; Determine directions for each corner
        corner-dirs (vec
                     (for [i (range n)]
                       (let [in-dir (segment-direction
                                     (nth oriented-points (mod (dec i) n))
                                     (nth oriented-points i))
                             out-dir (segment-direction
                                      (nth oriented-points i)
                                      (nth oriented-points (mod (inc i) n)))]
                         [in-dir out-dir])))

        ;; Compute offset for each corner based on incoming and outgoing directions
        corner-offset (fn [[in-dir out-dir]]
                        (case [in-dir out-dir]
                          [:west :south] [0 0]
                          [:south :east] [0 1]
                          [:east :north] [1 1]
                          [:north :west] [1 0]
                          [:south :west] [0 0]
                          [:west :north] [1 0]
                          [:north :east] [1 1]
                          [:east :south] [0 1]
                          [0 0]))

        ;; Build corners with offsets
        corners (vec
                 (for [i (range n)]
                   (let [[x y] (nth oriented-points i)
                         [in-dir out-dir] (nth corner-dirs i)
                         [dx dy] (corner-offset [in-dir out-dir])]
                     {:x (+ x dx) :y (+ y dy) :in in-dir :out out-dir})))

        ;; Build edges by connecting consecutive corners
        offset-segments
        (for [i (range n)]
          (let [c1 (nth corners i)
                c2 (nth corners (mod (inc i) n))
                dir (:out c1)]
            [[(:x c1) (:y c1)] [(:x c2) (:y c2)] dir]))

        ;; Extract horizontal edges (y constant, x varies)
        ;; Preserve the actual direction of travel (x1 to x2)
        horiz-edges (vec
                     (for [[[x1 y1] [x2 y2] dir] offset-segments
                           :when (= y1 y2)]
                       {:y y1
                        :x1 x1
                        :x2 x2
                        :dir dir}))

        ;; Extract vertical edges (x constant, y varies)
        ;; Preserve the actual direction of travel (y1 to y2)
        vert-edges (vec
                    (for [[[x1 y1] [x2 y2] dir] offset-segments
                          :when (= x1 x2)]
                      {:x x1
                       :y1 y1
                       :y2 y2
                       :dir dir}))]

    {:corners corners
     :horiz-edges horiz-edges
     :vert-edges vert-edges}))

;; Build rectangle from two opposite corners
(defn build-rectangle [[x1 y1] [x2 y2]]
  "Build a rectangle representation from two opposite corners in input format.
   Returns a shape with corners and edges going counterclockwise.
   Counterclockwise in (x right, y down): top-left -> top-right -> bottom-right -> bottom-left"
  (let [xmin (min x1 x2)
        xmax (max x1 x2)
        ymin (min y1 y2)
        ymax (max y1 y2)
        corners [{:x xmin :y ymin :in :west :out :south}
                 {:x xmin :y (inc ymax) :in :south :out :east}
                 {:x (inc xmax) :y (inc ymax) :in :east :out :north}
                 {:x (inc xmax) :y ymin :in :north :out :west}]
        horiz-edges [{:y (inc ymax) :x1 xmin :x2 (inc xmax) :dir :east}
                     {:y ymin :x1 xmin :x2 (inc xmax) :dir :west}]
        vert-edges [{:x xmin :y1 ymin :y2 (inc ymax) :dir :south}
                    {:x (inc xmax) :y1 ymin :y2 (inc ymax) :dir :north}]]
    {:corners corners
     :horiz-edges horiz-edges
     :vert-edges vert-edges}))

;; Check if a point is strictly between two values
(defn between? [x a b]
  (let [lo (min a b)
        hi (max a b)]
    (and (< lo x) (< x hi))))

(defn horiz-vert-intersect? [horiz-edge vert-edge]
  "Check if a horizontal edge and vertical edge properly intersect (not at endpoints).
   horiz-edge has {:y y, :x1 x1, :x2 x2}
   vert-edge has {:x x, :y1 y1, :y2 y2}"
  (let [{:keys [y x1 x2]} horiz-edge
        {:keys [x y1 y2]} vert-edge]
    (and (between? x x1 x2)
         (between? y y1 y2))))

(defn ccw [dir]
  ({:north :west, :west :south, :south :east, :east :north} dir))

(defn rot [object]
  (if (:dir object)
    ;; Edge
    {:dir (ccw (:dir object))}
    ;; Corner
    {:in (ccw (:in object)) :out (ccw (:out object))}))

(defn align [dir obj1 obj2]
  (case dir
    :north [obj1 obj2]
    :east [(rot obj1) (rot obj2)]
    :south [(rot (rot obj1)) (rot (rot obj2))]
    :west [(rot (rot (rot obj1))) (rot (rot (rot obj2)))]))

(defn check-corner? [rect-corner contour-edge]
  (let [[c e] (align (:in rect-corner) rect-corner contour-edge)]
    (contains? #{:north :west} (:dir e))))

(defn check-edge? [rect-edge contour-corner]
  (let [[e c] (align (:dir rect-edge) rect-edge contour-corner)]
    (contains? #{[:west :north] [:north :east]} [(:in c) (:out c)])))

(defn check-corners? [rect-corner contour-corner]
  (let [[rc cc] (align (:in rect-corner) rect-corner contour-corner)]
    (contains? #{[:north :west] [:south :west] [:west :north] [:north :east]} [(:in cc) (:out cc)])))

;; Helper functions to check if validation is needed

(defn corner-on-horiz-edge? [corner edge]
  "Check if a corner lies properly on a horizontal edge (not at endpoints)"
  (let [{cx :x cy :y} corner
        {:keys [y x1 x2]} edge]
    (and (= cy y)
         (between? cx x1 x2))))

(defn corner-on-vert-edge? [corner edge]
  "Check if a corner lies properly on a vertical edge (not at endpoints)"
  (let [{cx :x cy :y} corner
        {:keys [x y1 y2]} edge]
    (and (= cx x)
         (between? cy y1 y2))))

(defn corner-matches? [corner1 corner2]
  "Check if two corners are at the same position"
  (and (= (:x corner1) (:x corner2))
       (= (:y corner1) (:y corner2))))

;; Valid rectangle checker
(defn valid-rectangle? [rect contour]
  "Check if a rectangle is valid with respect to the contour.
   Returns true if the rectangle is entirely inside the region defined by the contour."
  (let [rect-horiz (:horiz-edges rect)
        rect-vert (:vert-edges rect)
        rect-corners (:corners rect)
        contour-horiz (:horiz-edges contour)
        contour-vert (:vert-edges contour)
        contour-corners (:corners contour)]

    ;; Check 1: No proper edge intersections
    (and
     ;; Check rect horizontal edges against contour vertical edges
     (every? (fn [rh]
               (not-any? #(horiz-vert-intersect? rh %) contour-vert))
             rect-horiz)
     ;; Check rect vertical edges against contour horizontal edges
     (every? (fn [rv]
               (not-any? #(horiz-vert-intersect? % rv) contour-horiz))
             rect-vert)

     ;; Check 2: Rectangle corners vs contour edges
     ;; Only check if corner actually lies on the edge
     (every? (fn [rc]
               (every? (fn [edge]
                         (if (corner-on-horiz-edge? rc edge)
                           (check-corner? rc edge)
                           true))
                       contour-horiz))
             rect-corners)
     (every? (fn [rc]
               (every? (fn [edge]
                         (if (corner-on-vert-edge? rc edge)
                           (check-corner? rc edge)
                           true))
                       contour-vert))
             rect-corners)

     ;; Check 3: Contour corners vs rectangle edges
     ;; Only check if corner actually lies on the edge
     (every? (fn [cc]
               (every? (fn [edge]
                         (if (corner-on-horiz-edge? cc edge)
                           (check-edge? edge cc)
                           true))
                       rect-horiz))
             contour-corners)
     (every? (fn [cc]
               (every? (fn [edge]
                         (if (corner-on-vert-edge? cc edge)
                           (check-edge? edge cc)
                           true))
                       rect-vert))
             contour-corners)

     ;; Check 4: Rectangle corners vs contour corners
     ;; Only check if corners are at the same position
     (every? (fn [rc]
               (every? (fn [cc]
                         (if (corner-matches? rc cc)
                           (check-corners? rc cc)
                           true))
                       contour-corners))
             rect-corners))))

(defn valid-pairs [input]
  (let [contour (input->contour input)
        pairs (for [[i1 p1] (map-indexed vector input)
                    [i2 p2] (map-indexed vector input)
                    :when (< i1 i2)]
                [p1 p2])]
    (filter
     (fn [[p1 p2]]
       (valid-rectangle? (build-rectangle p1 p2) contour))
     pairs)))

(defn part2 [input]
  (apply max (map (fn [[p1 p2]] (rectangle-area p1 p2)) (valid-pairs input))))

(defn solve []
  (let [input (-> (read-input) parse-input)]
    (println "Day 9")
    (println "Part 1:" (part1 input))
    (println "Part 2:" (part2 input))))