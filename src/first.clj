(ns first
  (:require [clojure.string :as str]))

(set! *warn-on-reflection* true)
(defn poly? [num]
  (= (str num) (str/reverse (str num))))

(defn max-n [n]
  (Integer/parseInt (apply str (repeat n \9))))

;; 1.1
(defn tail-rec-num-pal* [x y result]
  (let [res (* x y)]
    (cond
      (and (= x 0) (= y 0))
      result

      (= y 0)
      (recur (dec x) (dec x) result)

      (and (poly? res) (> res result))
      (recur x (dec y) res)

      :else
      (recur x (dec y) result))))

(defn tail-rec-num-pal [n]
  (tail-rec-num-pal* (max-n n) (max-n n) 0))

;; 1.2

(defn rec-num-pal* [x y result]
  (let [res (* x y)]
    (cond
      (and (= x 0) (= y 0))
      result

      (= y 0)
      (rec-num-pal* (dec x) (dec x) result)

      (and (poly? res) (> res result))
      (rec-num-pal* x (dec y) res)

      :else
      (rec-num-pal* x (dec y) result))))

(defn rec-num-pal [n]
  (rec-num-pal* (max-n n) (max-n n) 0))

;; 2 модульной реализации, где явно разделена генерация последовательности, фильтрация и свёртка (должны использоваться функции reduce/fold, filter и аналогичные);

(defn all-possible-results-for [n]
  (for [x (range (max-n n) (max-n (dec n)) -1)
        y (range x (max-n (dec n)) -1)]
    (* x y)))

(defn num-pal-modular [n]
  (->> (all-possible-results-for n)
       (filter poly?)
       (reduce max)))

;; 3 генерация последовательности при помощи отображения (map);

(defn num-pal-map [n]
  (->> (all-possible-results-for n)
       (map #(if (poly? %) % 0))
       (reduce max)))

;; 4 работа со спец. синтаксисом для циклов (где применимо);
(defn num-pal-loop [n]
  (loop [x (max-n n)
         y (max-n n)
         result (long 0)]
    (let [res (long (* x y))]
      (cond
        (and (= x 0) (= y 0))
        result

        (= y 0)
        (recur (dec x) (dec x) result)

        (and (poly? res) (> res result))
        (recur x (dec y) res)

        :else
        (recur x (dec y) result)))))

;; 5 работа с бесконечными списками для языков, поддерживающих ленивые коллекции или итераторы как часть языка

(defn num-pal-seq [n]
  (->> (all-possible-results-for n)
       (filter poly?)
       (sort >)
       (first)))

(comment

  (time (rec-num-pal 3)) ;; stackoverflow

  (with-out-str (time (tail-rec-num-pal 4))) ;; "\"Elapsed time: 4655.691704 msecs\"\n"
  (with-out-str (time (num-pal-modular 4))) ;; "\"Elapsed time: 4722.323622 msecs\"\n"
  (with-out-str (time (num-pal-map 4))) ;; "\"Elapsed time: 4519.871838 msecs\"\n"
  (with-out-str (time (num-pal-loop 4))) ;; "\"Elapsed time: 4695.93306 msecs\"\n"
  (with-out-str (time (num-pal-seq 4))) ;; "\"Elapsed time: 4448.769522 msecs\"\n"
  )
