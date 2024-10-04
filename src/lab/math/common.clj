(ns lab.math.common)

 ;; +------------------------------+
 ;; | генератор точек, для которых |
 ;; | необходимо вычислить         |
 ;; | аппроксимированное значение  |
 ;; +------------------------------+

(defn add-steps [x-seq step]
  (assert (> step 0))
  (range (apply min x-seq)
         (+ step (apply max x-seq)) step))

