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


(def PARMS1 [{:name "a"
              :required true
              :allowMultiple true
              :dataType "string" }
             {:name "b"
              :required true
              :allowMultiple false
              :dataType "int" }] )



(deftest testSignatureExplosion
  (testing "Multiple sigs exploded"
    (is (= [["Collection<String>" "int" "AriCallback"]
            ["Collection<String>" "int"]
            ["String" "int" "AriCallback"]
            ["String" "int"]]

           (explode-parms PARMS1)

           ))))



(deftest testSignatureTests
  (testing "hasCallback?"
    (is (true? (hasCallback? ["Collection<String>" "int" "AriCallback"])))
    (is (false? (hasCallback? ["Collection<String>" "int"]))))

  (testing "hasCallback?"
    (is (true?  (isMasterImplementation? PARMS1 ["Collection<String>" "int" "AriCallback"])))
    (is (false? (isMasterImplementation? PARMS1 ["String" "int" "AriCallback"]))))
  )






