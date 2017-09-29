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
  (testing "mkSection con stringa non vuota"
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
    (is (= "String" (swagger->java "string" ""))))
  (testing "esempio base: List of string"
    (is (= "List<String>" (swagger->java "List[string]" ""))))
  (testing "object: abstract"
    (is (= "Pluto" (swagger->java "Pluto" ""))))
  (testing "object:concrete"
    (is (= "Pluto_impl_V1" (swagger->java "Pluto" "V1"))))

)


(deftest classFileName
  (testing "Plain sample"
    (is (= "a/b/c.java" (genFilename "a.b" "c")))))


(deftest genAttributesTest
  (testing "One attribute"
    (is (= "int P" (genAttrs [{:name "P" :type "int"}]) )))
  (testing "Multiple attribute"
    (is (= "int P, String Q"
           (genAttrs [{:name "P" :type "int"} {:name "Q" :type "String"}]) )))

  )


(deftest testTypeAnnotation
  (testing "Anotazioni"
    (is (= "@JsonDeserialize( contentAs=String.class )"
           (mkSetterTypeAnnotation "List<String>")))
    (is (= ""
           (mkSetterTypeAnnotation "String")))

    ))

