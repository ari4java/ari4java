(ns codegen3.core
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hellox, World!"))



;; READ ARI FOLDER as JSON
(defn loadResourceFile
  "Loads a resource file; if missing, returns the default contents"
  [resource-path default-if-missing]
  (let [resource (clojure.java.io/resource resource-path)
        contents (cond
                    (nil? resource) default-if-missing
                    :else (slurp resource))]
    contents))


(defn readJson
  "Loads a JSON file as a Clojure structure"
  [ariVersion filename]
  (let [file  (str ariVersion "/" filename ".json")
        data  (loadResourceFile file "{}")]
    (json/read-str data :key-fn keyword)
  ))


