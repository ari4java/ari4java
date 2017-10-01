(defproject codegen3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "LGPL"}
  :dependencies [
                 [org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/spec.alpha "0.1.123"]
                 [org.clojure/test.check "0.9.0"]  ; should be in DEV
                 [orchestra "2017.08.13"]
                 [org.clojure/data.json "0.2.6"]
                 [com.rpl/specter "0.13.1"]]
  :main ^:skip-aot codegen3.core
  :plugins [[lein-codox "0.9.5"] 
            [lein-kibit "0.1.2"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
