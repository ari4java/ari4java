(defproject codegen3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "LGPL"}
  :dependencies [
  		[org.clojure/clojure "1.8.0"]
  		[org.clojure/data.json "0.2.6"] 
  	]
  :main ^:skip-aot codegen3.core
  :plugins [[lein-marginalia "0.8.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
