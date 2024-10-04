(ns lab.io
  (:require
   [clojure.string :as str]))

;; +---------------------------+
;; | обработка входного потока |
;; +---------------------------+

(set! *warn-on-reflection* true)

(defn point->csv [[x y]]
  (str (double x) "," (double y)))

(defn parse-csv-line [line]
  (try
    (let [[x y] (str/split line #",")]
      [(read-string x) (read-string y)])
    (catch Exception _
      (.println ^java.io.PrintWriter *err*
                (str "cannot parse line: '" line "'"))
      #_(System/exit 1))))

(defn read-batch [window-fn]
  (->> read-line
       repeatedly
       (take-while some?)
       window-fn))

;; +------------------------+
;; | печать выходных данных |
;; +------------------------+

(defn print-points! [points]
  (run! println (mapv point->csv points)))
