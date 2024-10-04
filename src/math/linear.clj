(ns math.linear
  (:require [lab.math.common :as common]))

(defn y [[x1 y1] [x2 y2] x]
  (+ (* (/ (- y2 y1)
           (- x2 x1))
        (- x x1))
     y1))

(defn y-all [dot1 dot2 & [step]]
  (->>
    (common/add-steps [(first dot1) (first dot2)] step)
    (map (fn [x] [x (y dot1 dot2 x)]))))

(defn linear [dots & [step]]
  (->> dots
       (partition 2 1)
       (map #(apply (fn [d1 d2] (y-all d1 d2 step)) %))
       (apply concat)))

(comment
  (linear [[1 1] [3 3] [5 5]] 0.5)
;; ([1 1]
;;  [1.5 1.5]
;;  [2.0 2.0]
;;  [2.5 2.5]
;;  [3.0 3.0]
;;  [3.5 3.5]
;;  [3 3]
;;  [3.5 3.5]
;;  [4.0 4.0]
;;  [4.5 4.5]
;;  [5.0 5.0]
;;  [5.5 5.5])

  (linear [[1 1] [3 3] [5 5]] 1) ;; ([1 1] [2 2] [3 3] [3 3] [4 4] [5 5])
  )
