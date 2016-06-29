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



