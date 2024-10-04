(ns build
  (:require [clojure.tools.build.api :as b]))

(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uberjar [{:keys [jar-name]
                :or   {jar-name "lab3"}}]
  (clean nil)

  (b/copy-dir {:src-dirs   ["src" "resources"]
               :target-dir class-dir})

  (b/compile-clj {:basis      basis
                  :ns-compile '[lab.core]
                  :class-dir  class-dir})

  (let [jar-path (str "target/" jar-name ".jar")]
    (b/uber {:class-dir class-dir
             :uber-file jar-path
             :basis     basis
             :main      'lab.core})

    (println "Building complete. Uberjar is available at:" jar-path)))

(comment
  (uberjar {}))

