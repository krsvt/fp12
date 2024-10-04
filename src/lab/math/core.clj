(ns lab.math.core
  (:require [lab.math.lagrange :as lagrange]
            [lab.math.linear :as linear]))

;; +------------------------+
;; | алгоритм аппроксимации |
;; +------------------------+

(defmulti method (fn [opts _ _] (:alg opts)))

(defmethod method "lagrange"
  [{step :step} points prev-points]
  (lagrange/lagrange
   points
   step
   (lagrange/find-which-to-skip prev-points points)))

(defmethod method "linear"
  [{step :step} points _]
  (linear/linear points step))

(defmulti method-window (fn [opts] (:alg opts)))

;; this can be changed, actually
(def lagrange-window-length 3)

(def lagrange-window (partial partition lagrange-window-length 1))

(defmethod method-window "lagrange" [_]
  lagrange-window)

; linear window is always 2
(def linear-window (partial partition 2 1))

(defmethod method-window "linear" [_] linear-window)
