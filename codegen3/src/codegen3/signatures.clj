(ns codegen3.signatures
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


(defn constrained-sqr [x]
    {:pre  [(pos? x)]
     :post [(> % 16), (< % 225)]}
    (* x x))
