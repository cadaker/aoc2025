(ns aoc2025.utils)

(defn pow
  "Computes base^exp using integer arithmetic.
  Returns a long for small results, automatically promotes to bigint if needed."
  [base exp]
  (cond
    (zero? exp) 1
    (= 1 exp) base
    :else (loop [result 1
                 b base
                 e exp]
            (if (zero? e)
              result
              (if (odd? e)
                (recur (*' result b) (*' b b) (quot e 2))
                (recur result (*' b b) (quot e 2)))))))