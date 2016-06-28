(defproject codegen3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "LGPL"}
  :dependencies [
  		[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot codegen3.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
