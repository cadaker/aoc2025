(ns aoc2025.grid)

(defrecord Grid [data width])

(defn grid-width [grid] (:width grid))

(defn grid-height [grid] (quot (count (:data grid)) (grid-width grid)))

(defn grid-contains? [grid row col]
  (and (<= 0 col (dec (grid-width grid)))
       (<= 0 row (dec (grid-height grid)))))

(defn grid-containsp? [grid [row col]]
  (grid-contains? grid row col))

(defn grid-get [grid row col]
  (let [index (+ (* row (grid-width grid)) col)]
    (get (:data grid) index)))

(defn grid-getp [grid [row col]]
  (grid-get grid row col))

(defn grid-assoc [grid row col val]
  (let [index (+ (* row (grid-width grid)) col)]
    (assoc-in grid [:data index] val)))

(defrecord GridBuilder [data width])

(defn make-gridbuilder []
  (->GridBuilder [] nil))

(defn gridbuilder-push [grid-builder x]
  (update-in grid-builder [:data] #(conj % x)))

(defn gridbuilder-eol [grid-builder]
  (let [width (:width grid-builder)
        item-count (count (:data grid-builder))]
    (cond
     (nil? width) (assoc grid-builder :width item-count)
     (zero? (rem item-count width)) grid-builder
     :else (throw (Exception. "inconsistent line lengths")))))

(defn gridbuilder-finish [grid-builder]
  (let [builder (gridbuilder-eol grid-builder)]
    (->Grid (:data builder) (:width builder))))

(defn grid-from-lines [lines]
  (gridbuilder-finish
    (reduce (fn [builder line]
              (gridbuilder-eol
                (reduce gridbuilder-push builder line)))
            (make-gridbuilder)
            lines)))

(defn grid-positions [grid]
  (for [row (range (grid-height grid))
        col (range (grid-width grid))]
    [row col]))

(defn grid-neighbors [grid row col]
  (filter #(grid-containsp? grid %)
          [[(dec row) col]
           [(inc row) col]
           [row (dec col)]
           [row (inc col)]]))

(defn grid-neighbors-8 [grid row col]
  (filter #(grid-containsp? grid %)
          [[(dec row) (dec col)]
           [(dec row) col]
           [(dec row) (inc col)]
           [row (dec col)]
           [row (inc col)]
           [(inc row) (dec col)]
           [(inc row) col]
           [(inc row) (inc col)]]))
