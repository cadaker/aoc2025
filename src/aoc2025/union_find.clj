(ns aoc2025.union_find)

(defn make-union-find [xs]
  (reduce
   (fn [uf x]
     (assoc uf x x))
   {}
   xs))

(defn lookup-no-update [uf x]
  (uf x))

(defn lookup [uf x]
  (loop [node x
         nodes-to-update []]
    (let [next (lookup-no-update uf node)]
      (if (= next node)
        [(reduce
          (fn [uf y]
            (assoc uf y next))
          uf
          nodes-to-update)
         next]
        (recur next (conj nodes-to-update node))))))

(defn join [uf x y]
  (let [[uf' y'] (lookup uf y)
        [uf'' x'] (lookup uf' x)]
    (assoc uf'' x' y')))
