(ns codegen3.javagen-test
  (:require [clojure.test :refer :all]
            [codegen3.javagen :refer :all]))

(deftest mkSection-nil
  (testing "mkSection con Nil"
    (is (= "" (mkSection "<" ">" nil)))))

(deftest mkSection-emptyString
  (testing "mkSection con stringa vuota"
    (is (= "<>" (mkSection "<" ">" "")))))

(deftest mkSection-string
  (testing "mkSection con stringa vuota"
    (is (= "<a>" (mkSection "<" ">" "a")))))

(deftest mkSection-list
  (testing "mkSection con stringa vuota"
    (is (= "<a><b><c>" (mkSection "<" ">" ["a" "b" "c"])))))

(deftest camelName-samples
  (testing "esempio base"
    (is (= "getBelloCiao" (camelName "get" "belloCiao" ))))
  (testing "un solo carattere"
    (is (= "getX" (camelName "get" "x" )))) )



(deftest typeTranslator-basics
  (testing "esempio base: string"
    (is (= "String" (typeTranslator "string" "" ))))
  (testing "esempio base: List of string"
    (is (= "List<String>" (typeTranslator "List[string]" "" ))))
  (testing "object: abstract"
    (is (= "Pluto" (typeTranslator "Pluto" "" ))))
  (testing "object:concrete"
    (is (= "Pluto_impl_V1" (typeTranslator "Pluto" "V1" ))))

)






