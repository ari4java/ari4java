(ns codegen3.signatures
  (:require [clojure.spec.alpha :as s]
           ; [clojure.spec.gen.alpha :as gen]
            [orchestra.spec.test :as st]
            [codegen3.javagen :as jg]
            [clojure.string :as str])
  (:gen-class))

;; compares method signatures
;; generate permutations

; :method     (camelName "get" v)
; :returns    t
; :args       [{:type :name}]
; :notes      (str "get " v)
; :body       (str "return this." v ";")


(defn getMethodSignature
  "Gets a Java signature as an array for our method
  e.g. public int pippo(int a, int b) ->
       [int pippo int int]
  "
  [{n :method t :returns a :args}]
  (let [args (map :type a)
        start [t n]] 
    (into start args)))

(defn getSignaturesForClass
  "Returns a list of signatures for my class"
  [{lFuncs :functions}]
  (map getMethodSignature lFuncs)
  ; AGGIUNGERE ASSERTIONS: metodi unici
  )



; //////////////////
(defn existing-string? [s]
  (and (-> s string?)
       (-> s count pos?)))

(s/def ::name existing-string?)
(s/def ::required boolean?)
(s/def ::allowMultiple boolean?)
(s/def ::dataType existing-string?)
(s/def ::fnparms (s/keys :req-un [::name ::required ::allowMultiple ::dataType] ))


(def ARICALLBACK "AriCallback")

; The most basic method (the one that has a full implementaion)
; is the one with multiple items and the ARI callback.
; When that is created, we create its aliases.

; (a int, b +str)
; ->
; (a int, b Collection<str>, AriCallBack cb)
; (a int, b str, AriCallBack)
; (a int, b  Collection<str> )
; (a int, b str)

(defn string-perms
  "Given an array of arrays of choices, produces
  all permutations.
  nil is a valid entry and will be removed, e.g.

  (string-perms [[:a :b] [:c]  [:d nil]] )
  => [[:a :c :d] [:a :c] [:b :c :d] [:b :c]]
  "
  ; use the single-parm version
  ([erest]
   (mapv :x (flatten (string-perms [] erest))))

  ; creates a seq of seqs
  ([ehead erest]
  (let [;_ (prn "H:" ehead " R:" erest " N:" (empty? erest))
        vs (first erest)]
      (cond
        (empty? erest)
        [{:x (filterv some? ehead)}]  ; remove nils
        :else
        (map
          #(string-perms (conj ehead %) (rest erest))
          vs)
        ))))


(s/fdef string-perms
        :args
        (s/or
          :arity1 (s/coll-of (s/and sequential?
                                    ;#(pos? (count %))
                                    )
                             :kind sequential? )
          :arity2 (s/cat :ehead sequential?
                         :erest sequential?)))


(defn explode-parms-permutations
  "Given a set of paramer types and whether they are multiple,
  builds possible permutations"
  [parms]
  (vec
    (for [{:keys [allowMultiple dataType]} parms
        :let [jDataType (jg/swagger->java dataType "")]]
    (cond
      (true? allowMultiple)
      [(str "Collection<" jDataType ">") jDataType]
      :else
      [jDataType]))))

(s/fdef explode-parms-permutations
        :args (s/cat :parms (s/coll-of ::fnparms :kind sequential? )))

(defn explode-parms
  "Returns all possible parameter permutations"
  [parms]
  (let [permutables (explode-parms-permutations parms)
        wAriCB      (into permutables [[ARICALLBACK nil]])]
    (string-perms wAriCB)))

(s/fdef explode-parms
        :args (s/cat :parms (s/coll-of ::fnparms :kind sequential? )))

(defn hasCallback?
  "Does this signature include the ARI CB"
  [parms]
  (= (last parms) ARICALLBACK))

(defn isMasterImplementation?
  "It is a 'master' implementation if:
  - has AriCallback
  - all multiple parameters are expressed as Collection
  "
  [parms lJavaTypes]
  (cond
    (hasCallback? lJavaTypes)
    ; check if thea are all true at the same time
    (= (map :allowMultiple parms)
       (map #(str/starts-with? % "Collection") (butlast lJavaTypes)))

    :else
      false

  ))

(s/fdef isMasterImplementation?
        :args (s/cat :parms (s/coll-of ::fnparms :kind sequential? )
                     :javaTypes (s/coll-of existing-string? :kind sequential?)
                     )

        )


;; Orchestra
(st/instrument)