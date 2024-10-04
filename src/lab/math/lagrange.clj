(ns lab.math.lagrange
  (:require [lab.math.common :as common]))

(defn lagrange-polynomial
  [points]
  (fn [x]
    (reduce
     (fn [sum i]
       (let [[xi yi] (nth points i)
             x-expr (reduce
                     (fn [prod j]
                       (if (not= i j)
                         (let [[xj _] (nth points j)]
                           (* prod (/ (- x xj) (- xi xj))))
                         prod))
                     1
                     (range (count points)))]
         (+ sum (* yi x-expr))))
     0
     (range (count points)))))

(defn lagrange
  ([points step skip-x-set]
   (let [lagrange-fn (lagrange-polynomial points)]
     (->> (common/add-steps (map first points) step)
          (remove #(skip-x-set %))
          (map (fn [x] [x (lagrange-fn x)])))))
  ([points step]
   (lagrange points step [])))

(defn find-which-to-skip [prev-points new-points]
  (if prev-points
    (let [prev-max (apply max (map first prev-points))]
      (set (filter #(<= (first %) prev-max) new-points)))
    #{}))

(comment
  (def points [[1 2] [2 4] [3 6]])
  (def lagrange-fn (lagrange-polynomial points))
  (lagrange-fn 1) ;; 2
  (lagrange-fn 1.5) ;; 3.0
  (lagrange-fn 2) ;; 4N
  (lagrange-fn 2.5) ;; 5.0
  (lagrange-fn 3) ;; 6
  (lagrange-fn 3.5) ;; 7.0
  (lagrange-fn 4) ;; 8N

  (lagrange [[1 1] [2 2] [3 3]] 1) ;; ([1 1] [2 2N] [3 3])
  (lagrange [[1 1] [2 2] [3 3]] 0.5) ;; ([1 1] [1.5 1.5] [2.0 2.0] [2.5 2.5] [3.0 3.0] [3.5 3.5])
  (lagrange points 0.5) ;; ([1 2] [1.5 3.0] [2.0 4.0] [2.5 5.0] [3.0 6.0] [3.5 7.0])

  (find-which-to-skip [[1 1] [2 2] [3 3] [4 4]]
                      [[2 2] [3 3] [4 4] [5 5]]) ;; ([2 2] [3 3] [4 4])
  )

