(ns second)

;; binary tree, dict
{:key 2
 :left  {:key 1 :left nil :right nil}
 :right {:key 3 :left nil :right nil}}

(defn make-node [key]
  {:key key :left nil :right nil})

(defn insert [tree key]
  (if (nil? tree)
    (make-node key)
    (let [node-key (:key tree)]
      (if (< key node-key)
        (assoc tree :left  (insert (:left tree) key))
        (assoc tree :right (insert (:right tree) key))))))

(defn find [tree key]
  (cond
    (nil? tree) nil
    (= key (:key tree)) tree
    (< key (:key tree)) (find (:left tree) key)
    :else (find (:right tree) key)))

(defn in-order [tree]
  (when tree
    (concat (in-order (:left tree))
            [(:key tree)]
            (in-order (:right tree)))))

(defn- new-from-col [col]
  (reduce insert nil col))

(defn my-remove [tree key]
  (new-from-col (remove #(= % key) (in-order tree))))

(defn my-map [f tree]
  (->> tree
       in-order
       (mapv f)
       (new-from-col)))

(defn foldl
  ([f init tree]
   (reduce f init (in-order tree)))
  ([f tree]
   (let [first-one (first (in-order tree))]
     (foldl f first-one (my-remove tree first-one)))))

(defn foldr
  ([f init tree]
   (loop [acc nil
          v (reverse (in-order tree)) start true]
     (cond
       (empty? v)
       (or acc init)

       start
       (recur (f (peek v) init) (pop v) false)

       :else
       (recur (f (peek v) acc) (pop v) false))))
  ([f tree]
   (let [col (reverse (in-order tree))
         last-one (first col)]
     (foldr f last-one (my-remove tree last-one)))))

(comment
  (def my-tree
    (-> (insert nil 10)
        (insert 5)
        (insert 15)
        (insert 3)
        (insert 7)))
  ;; {:key 10,
  ;;  :left
  ;;  {:key 5,
  ;;   :left {:key 3, :left nil, :right nil},
  ;;   :right {:key 7, :left nil, :right nil}},
  ;;  :right {:key 15, :left nil, :right nil}}

  (-> (my-remove my-tree 7)
      (find 7)) ;; nil

  (-> (my-remove my-tree 7)
      (find 5)) ;; nil



  (in-order (my-map inc my-tree)) ;; (4 6 8 11 16)


  (foldl + my-tree) ;; 40
  (foldl + 100 my-tree) ;; 140

  (foldr + my-tree) ;; 40
  (foldr + 100 my-tree) ;; 140

  (foldl - 0 my-tree) ;; -40
  (foldr - 0 my-tree) ;; 10


  )

