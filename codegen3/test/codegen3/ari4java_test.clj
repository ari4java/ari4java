(ns codegen3.ari4java-test
  (:require [clojure.test :refer :all]
            [codegen3.ari4java :refer :all]))



(deftest test-merge-property
  (testing "Empty property"
    (is (= {:type "a" :versions ["v1"] :name "pippo"}
           (merge-property nil {:type "a"} "pippo" "v1")
       )))

  (testing "Existing property"
    (is (= {:type "a" :versions ["v1" "v2"] :name "pippo"}
           (merge-property
             {:type "a" :versions ["v1"] :name "pippo"}
             {:type "a"} "pippo" "v2")
           )))

  (testing "Different type"
    (is (= "ExcRaised"
      (try
        (merge-property
            {:type "a" :versions ["v1"] :name "pippo"}
            {:type "x"} "pippo" "v2")
        (catch IllegalArgumentException e
               "ExcRaised")) )))

  )


(def modelProps

  [{:cljid :StasisEnd,
    :ver :ari_0_0_1,
    :isevt true,
    :id "StasisEnd",
    :description "Notification that a channel has left a Stasis application.",
    :properties {:channel {:required true, :type "Channel"}}}
    {:cljid :StasisEnd,
     :ver :ari_1_0_0,
     :isevt true,
     :id "StasisEnd",
     :description "Notification that a channel has left a Stasis application.",
     :properties {:channel {:required true, :type "Channel"}}} ])

(deftest test-props-set
  (testing ""
    (is (= {:versions [:ari_0_0_1 :ari_1_0_0]
            :name :channel
            :required true, :type "Channel"}
           (props-set modelProps :channel)
           )))
  )
