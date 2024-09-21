(ns test.second-test
  (:require
   [second :as sut]
   [clojure.spec.alpha :as s]
   [clojure.test :refer [is testing]]
   [clojure.test.check.clojure-test :refer [defspec]]
   [com.gfredericks.test.chuck.clojure-test :as chuck]))

(defn broken-sort [coll]
  (if (some #{13} coll)
    nil
    (sort coll)))

(defspec broken-sort-gen-test
  (chuck/for-all [input-coll (s/gen (s/coll-of int?))]
                 (let [output-coll (broken-sort input-coll)]
                   (testing "Result is in ascending order"
                     (when (seq input-coll)
                       (is (apply <= output-coll))))
                   (testing "The sorted collection contains the same elements"
                     (is (= (group-by identity input-coll)
                            (group-by identity output-coll)))))))

(defspec make-node-test
  (chuck/for-all
   [num (s/gen int?)]
   (let [node (sut/make-node num)]
     (testing "make-node"
       (is  (= node {:key num :left nil :right nil}))))))

(defspec insert-test
  (chuck/for-all
   [num (s/gen int?)]
   (let [node (sut/make-node num)              ; 1
         node-same (sut/insert node num)       ; 1 1
         node-inc (sut/insert node (inc num))  ; 1 2
         node-dec (sut/insert node (dec num))] ; 1 0

     (testing "adding same => insert into right"
       (is (= node-same
              {:key num
               :left nil
               :right {:key num :left nil :right nil}})))

     (testing "adding > => insert into right"
       (is (= node-inc
              {:key num
               :left nil
               :right {:key (inc num) :left nil :right nil}})))

     (testing "adding < => insert into left"
       (is (= node-dec
              {:key num
               :left  {:key (dec num) :left nil :right nil}
               :right nil}))))))

(defspec in-order-test
  (chuck/for-all
   [nums (s/gen (s/coll-of int?))]
   (def tree
     (reduce
      (fn [acc n] (sut/insert acc n))
      nil nums))

   (testing "in order is sorted"
     (when tree (is (apply <= (sut/in-order tree)))))))

(defspec delete-test
  (chuck/for-all
   [nums (s/gen (s/coll-of int?))]
   (def tree (reduce (fn [acc n] (sut/insert acc n)) nil nums))

   (testing "remove all => nil"
     (when tree (is (= nil
            (reduce
             (fn [acc n] (sut/my-remove acc n))
             tree
             nums)))))))

(comment

  (make-node-test)
  (insert-test)
  (in-order-test)
  (delete-test)

  (sut/my-remove (sut/make-node 1) 1))

;; DONE
;; 󰊕 make-node
;; 󰊕 insert
;; 󰊕 my-delete

;; TODO
;; 󰊕 find
;; 󰊕 in-order
;; 󰊕 new-from-col
;; 󰊕 my-map
;; 󰊕 foldl
;; 󰊕 foldr
