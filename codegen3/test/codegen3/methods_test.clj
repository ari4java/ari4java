(ns codegen3.methods-test
  (:require [clojure.test :refer :all]
            [codegen3.methods :refer :all]))

(def PARM {:name "applicationName"
           :description "descr"
           :paramType "path"
           :required true
           :allowMultiple false
           :dataType "string" })


(deftest plainParm
  (testing "Parametro semplice, tipo nativo"
    (is (= {:type "String", :name "applicationName", :comment "descr"}
           (mkParm PARM :PLAIN)))))
