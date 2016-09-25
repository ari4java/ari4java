(ns codegen3.signatures-test
  (:require [clojure.test :refer :all]
            [codegen3.signatures :refer :all]))



(deftest testSignature
  (testing "Signature for method with 2 parms"
    (is (= ["String" "hurrah" "int" "int"]
           (getMethodSignature
               {:method "hurrah"
                :returns "String"
                :args [{:type "int" :name "a"} {:type "int" :name "b"}]
               })


           ))))



