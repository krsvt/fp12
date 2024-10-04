(ns lab.math.linear)

(defn y [[x1 y1] [x2 y2] x]
  (+ (* (/ (- y2 y1)
           (- x2 x1))
        (- x x1))
     y1))

(defn y-all [p1 p2 & [step]]
  (when step (assert (> step 0)))
  (concat
    [p1]
    (->>
      (range (inc (first p1)) (first p2) (or step 1))
      (map (fn [x] [x (y p1 p2 x)])))
    [p2]))

(defn linear [always-two-points & [step]]
  (->> always-two-points
       (apply (fn [d1 d2] (y-all d1 d2 step)))))

(comment
  (linear [[1 1] [3 3]] 0.5) ;; ([1 1] [2 2] [2.5 2.5] [3 3])
  (linear [[1 1] [3 3]] 1) ;; ([1 1] [2 2] [3 3])
  )
