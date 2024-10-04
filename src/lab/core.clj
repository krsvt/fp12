(ns lab.core
  (:gen-class)
  (:require
   [lab.io]
   [clojure.tools.cli :as cli]
   [lab.math.core :as lab-math]))

;; Линейная интерполяция (по условию)
; csv: x1 = 1, x2 = 12, x3 = 15
; для линейной - нужны 2 точки. Окно размером в 2.
; взял 1 строку (1 число)
; берем вторую точку
; смотрим, сколько внутри точек надо рассчитать используя step из аргументов
; например, 10 точек: x1 = 1, x2 = 12, step = 1
; (1 2 3 4 5 6 7 8 9 10 11 12) - это не окно. смысла нет.
; нам не нужен y(11), когда мы ищем y(15), т.к. мы используем только y(12) и y(15)
; окно - [1, 12], след окно - [12, 15]
; значит, находим функцию по двум точкам, каждые 10 точек считаем,
; выводим каждую
; дальше берем третью точку
; окно - [12, 15]. далее аналогично.
;

;; Интерполяция Многочленом Лагранжа
; csv: (1,1) (3,3) (10,10) (11,11), (14,14)
; для лагранжа нужны минимум 3 точки, но можно сколько угодно.
; Окно можно дать задать из аргументов.
; пусть окно = 4
; берем 4 точки
; генерируем нужные x используя step из аргументов
; например, (1,1) (3,3) (10,10) (11,11), step = 1
; окно (1,3,10,11)
; используя эти известные точки, находим функцию полинома лагранжа f
; далее, рассчитываем для нужных x внутри 1-11 f(x)
; это f(2), f(4), f(5) ... f(9)
; и выводим каждую точку
; далее окно будет из точек (3,3) (10,10) (11,11) (14,14)
; и требуется найти y для точек (3 4 5 6 7 8 9 10 11 12 13)
; тут мы знаем значение точек 3-11 и 14, надо найти f'(12), f'(13)
; соответственно, ищем f' от точек (3,10,11,14) (можно было любые из 3-11, т.к. интерполяция)
; и вычисляем f'(12), f'(13), выводим
; и берем след окно

(def cli-options
  [["-s" "--step <num>" "Step"]
   ["-a" "--alg <num>" "Algorithm is one of: 'linear', 'lagrange'"]])

(defn print-errors [errors]
  (binding [*out* *err*]
    (doseq [e errors]
      (println "ERROR:" e))))

;; сохранение старых точек для алгоритмов, которым нужно.
;; как сделать так, чтобы read-batch возвращал предыдущие
;; точки в **функциональном стиле** и избавиться от стейта?
(def prev-points (atom nil))

(defn -main [& args]
  (let [{:keys [options _arguments summary errors]}
        (cli/parse-opts args cli-options)
        opts (update options :step parse-double)
        window (lab-math/method-window options)]

    (cond
      errors
      (print-errors errors)

      (empty? options)
      (do (println "lab3\n")
          (println summary))

      (:alg opts)
      (doseq [batch-points (lab.io/read-batch window)]
        (when-let [batch-points (and (seq batch-points)
                                     (map #(lab.io/parse-csv-line %)
                                          batch-points))]
          (->> (lab-math/method opts batch-points @prev-points)
               (lab.io/print-points!))
          (reset! prev-points batch-points)))
      :else
      (println "?"))))

(comment
  (with-in-str "1,1\n3,3\n5,5"
    (-main "-s" "0.5"
           "-a" "linear"))
  ; (out) 1.0,1.0
  ; (out) 2.0,2.0
  ; (out) 2.5,2.5
  ; (out) 3.0,3.0
  ; (out) 3.0,3.0
  ; (out) 4.0,4.0
  ; (out) 4.5,4.5
  ; (out) 5.0,5.0

  (with-in-str "1,1\n3,3\n5,5"
    (-main "-s" "0.5"
           "-a" "lagrange"))
  ; (out) 1.0,1.0
  ; (out) 1.5,1.5
  ; (out) 2.0,2.0
  ; (out) 2.5,2.5
  ; (out) 3.0,3.0
  ; (out) 3.5,3.5
  ; (out) 4.0,4.0
  ; (out) 4.5,4.5
  ; (out) 5.0,5.0
  )
