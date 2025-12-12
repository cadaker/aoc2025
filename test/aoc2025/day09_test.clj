(ns aoc2025.day09-test
  (:require [clojure.test :refer :all]
            [aoc2025.day09 :refer :all]))

(def example-input
  "7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3")

(deftest test-parse-input
  (testing "Parses coordinate strings into vectors of numbers"
    (let [parsed (parse-input example-input)]
      (is (= 8 (count parsed)))
      (is (= [7 1] (first parsed)))
      (is (= [7 3] (last parsed)))
      (is (every? #(= 2 (count %)) parsed)))))

(deftest test-example
  (testing "Example case: largest rectangle has area 50"
    (let [parsed (parse-input example-input)]
      (is (= 50 (part1 parsed))))))

(def example-points
  [[7 1]
   [11 1]
   [11 7]
   [9 7]
   [9 5]
   [2 5]
   [2 3]
   [7 3]])

(deftest test-segment-direction
  (testing "Segment direction calculation"
    (is (= :east (segment-direction [7 1] [11 1])))
    (is (= :south (segment-direction [11 1] [11 7])))
    (is (= :west (segment-direction [11 7] [9 7])))
    (is (= :north (segment-direction [9 7] [9 5])))))

(deftest test-input->contour
  (testing "Convert example input to contour"
    (let [contour (input->contour example-points)]
      ;; Should have 8 corners (one for each input point)
      (is (= 8 (count (:corners contour))))
      (is (= {:x 7 :y 3 :in :south :out :west} (nth (:corners contour) 0)))
      (is (= {:x 2 :y 3 :in :west :out :south} (nth (:corners contour) 1)))
      (is (= {:x 2 :y 6 :in :south :out :east} (nth (:corners contour) 2)))
      (is (= {:x 9 :y 6 :in :east :out :south} (nth (:corners contour) 3)))
      (is (= {:x 9 :y 8 :in :south :out :east} (nth (:corners contour) 4)))
      (is (= {:x 12 :y 8 :in :east :out :north} (nth (:corners contour) 5)))
      (is (= {:x 12 :y 1 :in :north :out :west} (nth (:corners contour) 6)))
      (is (= {:x 7 :y 1 :in :west :out :south} (nth (:corners contour) 7)))

      (is (= 4 (count (:horiz-edges contour))))
      (is (= {:y 3 :x1 7 :x2 2 :dir :west} (nth (:horiz-edges contour) 0)))
      (is (= {:y 6 :x1 2 :x2 9 :dir :east} (nth (:horiz-edges contour) 1)))
      (is (= {:y 8 :x1 9 :x2 12 :dir :east} (nth (:horiz-edges contour) 2)))
      (is (= {:y 1 :x1 12 :x2 7 :dir :west} (nth (:horiz-edges contour) 3)))

      (is (= 4 (count (:vert-edges contour))))
      (is (= {:x 2 :y1 3 :y2 6 :dir :south} (nth (:vert-edges contour) 0)))
      (is (= {:x 9 :y1 6 :y2 8 :dir :south} (nth (:vert-edges contour) 1)))
      (is (= {:x 12 :y1 8 :y2 1 :dir :north} (nth (:vert-edges contour) 2)))
      (is (= {:x 7 :y1 1 :y2 3 :dir :south} (nth (:vert-edges contour) 3))))))

(deftest test-build-rectangle
  (testing "Build rectangle from two corners"
    (let [rect (build-rectangle [2 5] [9 7])]
      ;; Should have 4 corners
      (is (= 4 (count (:corners rect))))
      ;; Should have 2 horizontal and 2 vertical edges
      (is (= 2 (count (:horiz-edges rect))))
      (is (= 2 (count (:vert-edges rect)))))))

(deftest test-between?
  (testing "between? predicate"
    (is (between? 5 3 7))
    (is (between? 5 7 3))
    (is (not (between? 3 3 7)))
    (is (not (between? 7 3 7)))
    (is (not (between? 2 3 7)))
    (is (not (between? 8 3 7)))))

(deftest test-simple-square-contour
  (testing "Simple 2x2 square contour"
    (let [;; A square: (0,0) -> (0,1) -> (1,1) -> (1,0)
          square [[0 0] [0 1] [1 1] [1 0]]
          contour (input->contour square)]

      (is (= 4 (count (:corners contour))))
      (is (= {:x 0 :y 0 :in :west :out :south} (nth (:corners contour) 0)))
      (is (= {:x 0 :y 2 :in :south :out :east} (nth (:corners contour) 1)))
      (is (= {:x 2 :y 2 :in :east :out :north} (nth (:corners contour) 2)))
      (is (= {:x 2 :y 0 :in :north :out :west} (nth (:corners contour) 3)))

      (is (= 2 (count (:horiz-edges contour))))
      (is (= {:y 2 :x1 0 :x2 2 :dir :east} (nth (:horiz-edges contour) 0)))
      (is (= {:y 0 :x1 2 :x2 0 :dir :west} (nth (:horiz-edges contour) 1)))

      (is (= 2 (count (:vert-edges contour))))
      (is (= {:x 0 :y1 0 :y2 2 :dir :south} (nth (:vert-edges contour) 0)))
      (is (= {:x 2 :y1 2 :y2 0 :dir :north} (nth (:vert-edges contour) 1))))))

(deftest test-example-rectangles
  (testing "Example rectangles from problem statement"
    ;; Rectangle with area 24 between [2 5] and [9 7]
    ;; Expected area: (9-2+1) * (7-5+1) = 8 * 3 = 24
    (let [rect1 (build-rectangle [2 5] [9 7])]
      (is (= 4 (count (:corners rect1))))
      (is (= 2 (count (:horiz-edges rect1))))
      (is (= 2 (count (:vert-edges rect1)))))

    ;; Rectangle with area 35 between [7 1] and [11 7]
    ;; Expected area: (11-7+1) * (7-1+1) = 5 * 7 = 35
    (let [rect2 (build-rectangle [7 1] [11 7])]
      (is (= 4 (count (:corners rect2))))
      (is (= 2 (count (:horiz-edges rect2))))
      (is (= 2 (count (:vert-edges rect2)))))

    ;; Largest rectangle with area 50 between [2 5] and [11 1]
    ;; Expected area: (11-2+1) * (5-1+1) = 10 * 5 = 50
    (let [rect3 (build-rectangle [2 5] [11 1])]
      (is (= 4 (count (:corners rect3))))
      (is (= 2 (count (:horiz-edges rect3))))
      (is (= 2 (count (:vert-edges rect3)))))))

(deftest test-valid-rectangle
  (testing "Valid rectangle checking with example contour"
    (let [contour (input->contour example-points)]

      ;; Valid rectangles (part 2 examples)
      (testing "Valid rectangles should return true"
        (is (true? (valid-rectangle? (build-rectangle [7 3] [11 1]) contour))
            "Rectangle [7 3] to [11 1] (area 15) should be valid")
        (is (true? (valid-rectangle? (build-rectangle [9 7] [9 5]) contour))
            "Rectangle [9 7] to [9 5] (area 3) should be valid")
        (is (true? (valid-rectangle? (build-rectangle [9 5] [2 3]) contour))
            "Rectangle [9 5] to [2 3] (area 24, largest valid) should be valid"))

      ;; Invalid rectangles (extend beyond green tiles)
      (testing "Invalid rectangles should return false"
        (is (false? (valid-rectangle? (build-rectangle [2 5] [9 7]) contour))
            "Rectangle [2 5] to [9 7] (area 24) should be invalid")
        (is (false? (valid-rectangle? (build-rectangle [2 5] [11 1]) contour))
            "Rectangle [2 5] to [11 1] (area 50) should be invalid")
        (is (false? (valid-rectangle? (build-rectangle [7 1] [11 7]) contour))
            "Rectangle [7 1] to [11 7] (area 35) should be invalid")))))

(deftest test-part2-example
  (testing "Part 2 with example input"
    (let [parsed (parse-input example-input)]
      ;; According to the problem, the largest valid rectangle has area 24
      (is (= 24 (part2 parsed))
          "Part 2 should find the largest valid rectangle with area 24"))))
