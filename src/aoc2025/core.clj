(ns aoc2025.core
  (:require [clojure.stacktrace :as st])
  (:gen-class))

(defn -main
  [& args]
  (if (empty? args)
    (println "Usage: lein run <day>")
    (let [day (first args)
          day-ns (symbol (str "aoc2025.day" (format "%02d" (Integer/parseInt day))))]
      (try
        (require day-ns)
        ((ns-resolve day-ns 'solve))
        (catch Exception e
          (println "Error running day" day ":")
          (st/print-cause-trace e))))))
