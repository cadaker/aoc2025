(ns aoc2025.union-find-test
  (:require [clojure.test :refer :all]
            [aoc2025.union_find :refer :all]))

(deftest test-make-union-find
  (testing "Creates union-find structure with each element as its own root"
    (let [uf (make-union-find [1 2 3 4])]
      (is (= {1 1, 2 2, 3 3, 4 4} uf)))))

(deftest test-lookup-no-update
  (testing "Simple lookup returns parent of element"
    (let [uf {1 1, 2 1, 3 2}]
      (is (= 1 (lookup-no-update uf 1)))
      (is (= 1 (lookup-no-update uf 2)))
      (is (= 2 (lookup-no-update uf 3))))))

(deftest test-lookup-root-element
  (testing "Lookup of root element returns itself"
    (let [uf (make-union-find [1 2 3])
          [uf' root] (lookup uf 1)]
      (is (= 1 root))
      (is (= uf uf')))))

(deftest test-lookup-with-path
  (testing "Lookup follows path to root and compresses path"
    (let [uf {1 1, 2 1, 3 2, 4 3}
          [uf' root] (lookup uf 4)]
      (is (= 1 root))
      (is (= 1 (uf' 4)))
      (is (= 1 (uf' 3)))
      (is (= 1 (uf' 2))))))

(deftest test-join-two-elements
  (testing "Join connects two separate elements"
    (let [uf (make-union-find [1 2 3 4])
          uf' (join uf 1 2)
          [_ root1] (lookup uf' 1)
          [_ root2] (lookup uf' 2)]
      (is (= root1 root2)))))

(deftest test-join-multiple-elements
  (testing "Multiple joins create a single connected component"
    (let [uf (make-union-find [1 2 3 4 5])
          uf' (-> uf
                  (join 1 2)
                  (join 2 3)
                  (join 3 4))
          [_ root1] (lookup uf' 1)
          [_ root2] (lookup uf' 2)
          [_ root3] (lookup uf' 3)
          [_ root4] (lookup uf' 4)
          [_ root5] (lookup uf' 5)]
      (is (= root1 root2 root3 root4))
      (is (not= root1 root5)))))

(deftest test-separate-components
  (testing "Elements in different components have different roots"
    (let [uf (make-union-find [1 2 3 4])
          uf' (-> uf
                  (join 1 2)
                  (join 3 4))
          [_ root1] (lookup uf' 1)
          [_ root2] (lookup uf' 2)
          [_ root3] (lookup uf' 3)
          [_ root4] (lookup uf' 4)]
      (is (= root1 root2))
      (is (= root3 root4))
      (is (not= root1 root3)))))

(deftest test-join-already-connected
  (testing "Joining already connected elements is idempotent"
    (let [uf (make-union-find [1 2 3])
          uf' (join uf 1 2)
          [_ root-before] (lookup uf' 1)
          uf'' (join uf' 1 2)
          [_ root-after] (lookup uf'' 1)]
      (is (= root-before root-after)))))

(deftest test-join-non-root-element
  (testing "Join must find root of both elements before connecting"
    (let [uf (make-union-find [0 1 2])
          uf' (join uf 0 1)
          uf'' (join uf' 0 2)
          [_ root0] (lookup uf'' 0)
          [_ root1] (lookup uf'' 1)
          [_ root2] (lookup uf'' 2)]
      (is (= root0 root1 root2) "All three elements should be in the same component"))))