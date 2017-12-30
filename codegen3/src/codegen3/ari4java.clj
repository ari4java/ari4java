(ns codegen3.ari4java
  (:require [codegen3.javagen :as jg]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [codegen3.signatures :as sgn]))

;; This namespace runs the translation of
;; a swagger database (e.g. sample_db.clj) as read in core
;; into an array of Java class definitions that will
;; then be written by javagen.clj

;; LOGIC
;; We distinguish two kinds of objects:
;; - Actions: these are the actual objects that
;;   are used to perform actions. When you run A4j
;;   you use an interface, but the runtime object
;;   is bound to the version you specified.
;;   They are named e.g. "ActionApplications" and the
;;   runtime version will be "ActionApplication_impl_{ariVer}"
;; - Models: these are data containers coming from Asterisk.
;;   They are "only" data continers, so have no meaningful
;;   method on themselves. We add .toString, .equals and
;;   .hashCode just in case.


;; Method Permutations
;; ....


;; INTERFACES
;; .a4j.generated
;;
;; Actions: -
;;
;; Models:
;;  e.g. (-> DB :ari_1_0_0 :applications :models :Application)
;;  At the interface level, we make it a map
;;  {"field" "swaggerType"}
;; To actually generate the model, we also need:
;;  - All the ARI version(s) it is present in
;;  - The description, as taken from most recent version
;;
;; A fixed set of models extend EventSource (for tagging)
;;
;; \TODO which models are "extends EventSource"?




(def IMPORTS_GENERATED_MODELS [
                               "ch.loway.oss.ari4java.generated.*"
                               "com.fasterxml.jackson.databind.annotation.JsonDeserialize"
                               "java.util.Date"
                               "java.util.List"
                               "java.util.Map" ])



(def IMPORTS_INTERFACES_MODELS [
                                "java.util.Date"
                                "java.util.List"
                                "java.util.Map"
                                "java.util.ArrayList"
                                "ch.loway.oss.ari4java.tools.RestException"
                                "ch.loway.oss.ari4java.tools.AriCallback"
                                "ch.loway.oss.ari4java.tools.tags.*"
                                ])


(defn eventSources
  "At the moment the list of EventSources is fixed.
  But it would be better to read it from the DB."
  []
  #{ :Bridge :Channel :Endpoint :DeviceState })

(defn isEvtSource
  "A set can be looked-up as a function of its members"
  [model]
  (some? ((eventSources) model)))


(defn models-for
  "A list of models for an ARI version.
  Models are held in:
    -> DB :ari_1_0_0 :applications :models
    -> DB :ari_1_0_0 :events :models

  Returns a map of models indexed by  model name.
  "
  [DB version]
  (let [entries (-> DB version keys)]
    (reduce into {}
            (for [entry entries]
              (-> DB version entry :models)))))


(defn model-names-for
  [DB version]
  (keys (models-for DB version)))


(defn all-ari-versions [db]
  (keys db))


(defn all-possible-model-names
  "Returns a list of all possible model names across all ARI versions"
  [DB]
  (reduce into #{}
          (map (partial model-names-for DB)
               (all-ari-versions DB))))


(defn get-model- [DB version name]
  "Reads a model given a version and a name.
  Annotates the ARI model with whether it's an event and
  the ARI version it comes from"

  (let [am (-> DB version :applications :models name)
        em (-> DB version :events       :models name)]

    (cond
      (and (nil? em) (nil? am))
        nil
      (nil? em)
        (into {:cljid name :ver version :isevt false} am)
      :else
        (into {:cljid name :ver version :isevt true}  em))))


(defn get-model [DB version name]
  "Reads a model given a version and a name.
  Annotates the ARI model with whether it's an event and
  the ARI version it comes from"

  (let [entries  (-> DB version keys)
        lzModels (map #(-> DB version % :models name) entries)
        found    (first (filter some? lzModels))]
    (cond
      (nil? found)    nil
      :else           (into {:cljid name :ver version :isevt false} found)
      )))


    (defn merge-property
  "Derives an annotated property from an existing annotated
  property p1 + a non-annotated property and a version.
  An annotated property has a name and
  a list of versions it applies to.
  "
  [ap1 p2 name version]

  ;(prn "mergeprop" ap1 p2 name version)

  (cond
    (nil? ap1)
      (into {:versions [version] :name name} p2)

    :else
      (cond
        (= (:type ap1) (:type p2))
          (into ap1  {:versions (conj (:versions ap1) version)})

        :else-exception
          (throw (IllegalArgumentException.
                   (str "Different type in version: " version " for ap1:" ap1 " p2:" p2 ))))
))



(defn props-set
  "Given a list of models and a prop to extract,
  gets us the property."
  [vModels prop]
  (let [allprops (map
                   (fn [model]
                     {:ver  (:ver model)
                      :name prop
                      :prop (get-in model [:properties prop])})
                      vModels)
        ; a prop might not exist
        allexistingprops (filter #(some? (:prop %)) allprops)

                   ]

    (reduce (fn [a v] (merge-property a (:prop v) prop (:ver v)))
            nil
            allexistingprops)
    )

  )


(defn model-info
  "What do we know about this model across our entire database?

  "
  [DB name]
  (let [models (map #(get-model DB % name) (all-ari-versions DB))
        found  (filter some? models)
        versions (map :ver found)
        lastmod  (last found)
        allpropnames (keys (reduce into {} (map :properties found)))
        allprops  (map (partial props-set found) allpropnames)
        ]
    {:model    name
     :versions (vec versions)
     :dscr     (:description lastmod)
     :evt      (:isevt       lastmod)
     :properties (vec allprops)
     ;:_fnd      found
     }
    ))


(defn get-property-description [prop]
  (str (:description prop)
       "\n"
       "\n"
       "Supported versions: " (:versions prop)
       )

  )


(defn get-model-description [prop]
  (str (:dscr prop)
       "\n"
       "\n"
       "Supported versions: " (:versions prop)
       "\n"
       "DEBUG:" prop "\n"
       )

  )

(defn generate-model-concrete
  [model]
  (jg/mkDataClass
    "ch.loway.oss.ari4java.generated"
    (name (:model model))

    (get-model-description model)

    (mapv
      (fn [prop]
        {:type (jg/swagger->java
                 (:type prop) "")
         :name (name (:name prop))
         :comment (get-property-description prop)})
      (:properties model)

      )))

(defn generate-model-interface
  [model]
  (let [modelName (:model model)]

    (jg/mkDataClassInterface
      "ch.loway.oss.ari4java.generated"
      (name modelName)
      (if (isEvtSource modelName) " EventSource " nil ) ;; extends
      [] ;; implements
      IMPORTS_INTERFACES_MODELS
      (get-model-description model)

      (mapv
        (fn [prop]
          {:type (jg/swagger->java
                   (:type prop) "")
           :name (name (:name prop))
           :comment (get-property-description prop)
           :interfaceonly true })
        (:properties model)

        ))



    )

  )


(defn generate-model-interfaces
  "Builds all model interfaces.
  "
  [DB]
  (let [models (map (partial model-info DB) (all-possible-model-names DB) )]
    (map generate-model-interface  models)))


(defn generate-model-implementation
  "An implementaion is like an interface, but:
  - if a field is not supported, it should throw
    an error
  - it has toString and hashCode methods added in

  "
  [model ari]

  (jg/mkDataClass
  ;(vec [

    (str "ch.loway.oss.ari4java.generated." (name ari) ".models")
    (str (name (:model model)) "_impl_" (name ari))
    nil ;; extends
    [ (name (:model model)) ] ;; implements
    IMPORTS_GENERATED_MODELS
    (get-model-description model)

    (mapv
      (fn [prop]
        {:type (jg/swagger->java
                 (:type prop) "")
         :name (name (:name prop))
         :comment (get-property-description prop)
         :notimplemented (not (some #{ari}  (:versions prop) )) }
        )
      (:properties model))

  ;      ]

      ))




(defn generate-model-implementations [DB]
  (let [allaris (all-ari-versions DB)
        allmodels (all-possible-model-names DB)
        models  (map (partial model-info DB) allmodels)]
    (for [m models a allaris]
      (generate-model-implementation m a)
      ;allaris

      )))


(s/def ::odb_entry
  (s/keys :req-un [::op_path
                   ::op_description
                   ::op_version
                   ::op_action
                   ::httpMethod
                   ::summary
                   ::nickname
                   ::responseClass
                   ::parameters
                   ]))

(s/def ::odb
  (s/coll-of ::odb_entry :kind sequential? ))




;; ACTIONS
;; In order to work on actions, we first transform the Actions database
;; so that it is a large list of Operations
;; enriched with:
;; :op_path
;; :op_description
;; :op_version        :ari_0_0_1
;; :op_type           :bridges

(defn dbDescr
  "Loads a description of the DB in terms of aris and entities"

  [DB]
  (let [allaris (all-ari-versions DB)
        allentities (reduce into #{}
                            (map #(-> DB % keys) allaris)) ]
    {:aris  (vec (sort allaris))
     :ents  (vec (sort allentities))} ))


(s/fdef opsDb
        :ret  ::odb)


(defn opsDb
  "Loads a flat operations DB that can be easily filtered.
  As sometimes some keys are missing, we add them in."
  [DB]
  (let [{aris :aris ents :ents} (dbDescr DB)]
    (vec
      (flatten
        (for [ari aris
              ent ents]
          (let [apis (-> DB ari ent :apis)]
            (for [api apis]
              (let [path  (:path api)
                    descr (:description api)
                    ops   (:operations  api)]
                (map
                  (fn [op] (let [parms (get op :parameters [])
                                 newParms (map #(into {:required true :allowMultiple false} %) parms) ]
                             (into {:op_path        path
                                  :op_description descr
                                  :op_version     ari
                                  :op_action      ent
                                  ; sometimes those are missing
                                  :parameters     []
                                  } (into op {:parameters newParms}))))
                  ops)))))))))


;; Here we have a litte tool to understand the contents of our DB

;; (aj/findParams (aj/opsDb DB)  #(= "operation" (:name %) )  )
(defn findParams
  [odb pred]
  (filter
    #(pos? (count (filter pred (:parameters %)) )   )
    odb
    ))

(defn viewMethod [odb ari name]

  (let [predAri (cond
                  (nil? ari)   #(= 1 1)
                  :else        #(= ari (:op_version %)))
        predName #(= name (:nickname %))
        ]


  (filter
    #(and (predAri %) (predName %))
    odb
    )))


; How many methods have multiple parameters? just a few
;
;(map #(vec [(:op_action %) (:nickname %)])
;     (aj/findParams
;       (filter #(= :ari_1_8_0 (:op_version %)) (aj/opsDb DB))
;       #(= true (:allowMultiple %) )  ))
;
; ([:applications "subscribe"]
;  [:applications "unsubscribe"]
;  [:asterisk "getInfo"]
;  [:bridges "addChannel"]
;  [:bridges "removeChannel"]
;  [:events "eventWebsocket"]
;  [:events "userEvent"])

; How many methods have enums?
;(map #(vec [(:op_action %) (:nickname %)])
;     (aj/findParams
;       (filter #(= :ari_1_8_0 (:op_version %)) (aj/opsDb DB))
;       #(= "LIST" (-> % :allowableValues :valueType) )  ))
;
; ([:asterisk "getInfo"]
;  [:bridges "record"]
;  [:channels "hangup"]
;  [:channels "mute"]
;  [:channels "unmute"]
;  [:channels "record"]
;  [:channels "snoopChannel"]
;  [:channels "snoopChannelWithId"]
;  [:deviceStates "update"]
;  [:playbacks "control"])



;; ----------  Enums ---------
; Enums have a name that matches the field they go in.
; If multiple enums have the same name, their fields are coalesced.
;
; To find enums, we scan all fields for ":allowableValues :valueType" = "LIST"
; Enums are produced as a map of arrays:
; {e1 [v1 v2 v3], e2 [v1 v2 v3]}

(defn find-enums
  [odb]
  (let [parms (flatten (map :parameters odb))
        wEnum (filter #(= "LIST" (-> % :allowableValues :valueType) ) parms)
        tuples (map (fn [p]  [(:name p) (set (-> p :allowableValues :values)) ])  wEnum)
        groups (group-by first tuples)
        mapk   (map  (fn [[k v]]  [k  (reduce into #{} (map second v)) ] )  groups)
        ]

    (into {} mapk)))


(defn generate-enum-classes [odb]
  (let [ens (find-enums odb)]

    (map
      (fn [[k v]]
        (jg/mkEnum "ch.loway.oss.ari4java.generated.enums" k  "-" v))
      ens
      )))


; ==================
; ARIBUILDERS and such things

(defn generate-ari-builder-interface
  "public abstract ActionApplications actionApplications();"
  [allmods allactions]
  (let [allObj (sort (into allmods allactions))]

    {
     :isInterface  true
     :classname    "AriBuilder"
     :package      "ch.loway.app.oss.ari4java.generated"
     :imports      [ "ch.loway.oss.ari4java.ARI" ]
     :notes        ""
     :stanzas     (into
                    ["public abstract ARI.ClassFactory getClassFactory();"]
                    (map #(str "public abstract "
                               (jg/className (name %))
                               " "
                               (jg/lcName (name %))
                               "();")
                         allObj))
    }))

(defn generate-ari-builder-impl
  "public abstract ActionApplications actionApplications();"
  [version DB allmods allactions]
  (let [allObj (sort (into allmods allactions))
        ver    (name version)
        pkg    (str "ch.loway.app.oss.ari4java.generated." ver)
        knownModels (set (model-names-for DB version))
        ]

    {
     :classname    (str "AriBuilder_impl_" (name version))
     :package      pkg
     :imports      [ (str pkg ".models.*;")
                    (str pkg ".actions.*;")
                    "ch.loway.oss.ari4java.generated.*"
                    "ch.loway.oss.ari4java.ARI" ]
     :implements   "AriBuilder"
     :notes        ""
     :functions    (map (fn [x]
                          {:method      (jg/lcName (name x))
                           :returns     (jg/className (name x))
                           :body (cond
                                   (knownModels x)

                                        (str "return new "
                                      (jg/className (name x))
                                      "_impl_"
                                      ver
                                      "();")

                                    :else
                                      "throw new UnsupportedOperationException();")

                           })
                        allObj)
      :stanzas     (str "public ARI.ClassFactory getClassFactory() {\n"
                        "return new ClassTranslator_impl_" ver  "();\n};\n")
     }))


(defn generate-class-translator-impl
  ""
  [version DB allmods allactions]
  (let [allObj (sort (into allmods allactions))
        ver    (name version)
        pkg    (str "ch.loway.app.oss.ari4java.generated." ver)
        knownModels (set (model-names-for DB version))
        ]

    {
     :classname    (str "ClassTranslator_impl_" (name version))
     :package      pkg
     :imports      [ (str pkg ".models.*;")
                    (str pkg ".actions.*;")
                    "ch.loway.oss.ari4java.generated.*"
                    "ch.loway.oss.ari4java.ARI" ]
     :implements   "ARI.ClassFactory"
     :notes        ""
     :stanzas     (str "@Override\n"
                       "public Class getImplementationFor(Class interfaceClass) { \n"
                       (apply str (map (fn [o] (str "if ( interfaceClass.equals("
                                         (name o) ".class) ) {\n"
                                         "return (" (name o) "_impl_" ver ".class);\n"
                                         "} else \n")) allObj))
                       "{\n      return null;\n    } }"

                       )
     }))




(defn generate-ari-builders
  "We generate a generic AriBuilder interface for
  all models plus an implementor for each ARI version."
  [DB]
  (let [allaris (all-ari-versions DB)
        allmods (all-possible-model-names DB)
        allacts [] ]

    (flatten
      [(generate-ari-builder-interface allmods allacts)
      (map #(generate-ari-builder-impl % DB allmods allacts) allaris)
      (map #(generate-class-translator-impl % DB allmods allacts) allaris)]

      )))


; =================================================================
; ACTIONS
; Generation of all actions is based on an opsDb
; when testing, you can:
;    (def ODB (opsDb (codegen3.core/readAll)))
;    (allSignaturesForAction ODB :applications)



(defn allActions [ODB]
  (->> ODB
       (map :op_action)
       set
       sort
       vec ))

(s/fdef allActions
        :args (s/cat :odb ::odb)
        :ret  vector?)


; op_entity -> op_action


(s/fdef signature
        :args (s/cat :dbe ::odb_entry))

(defn signature
  "Gets a signature tuple. [{:nick....} :ari_0]
  As :parameters might not exist, we read it through a get
  so we can put a default."
  [dbe]
  (let [p (get dbe :parameters [])]
    [{:nickname      (:nickname dbe)
      :parms         (sgn/explode-parms-permutations p)
      :responseClass (:responseClass dbe)
      }
     (:op_version dbe)]
    ))

(s/def ::tuple
  (s/and vector?
         #(= 2 (count %)) ))

(s/fdef groupTuples
    :args (s/cat :tuples (s/coll-of ::tuple ))
    :ret map?)

(defn groupTuples
  "Translates a sequence of [ [item tag]...]
  into a map {item [tag...]}
  "
  [sTuples]
  (into {}
        (map
          (fn [[k v]] [k (mapv second v)] )
          (group-by first sTuples))))


(s/fdef allSignaturesForAction
        :args (s/cat :dbe ::odb
                     :action keyword?))

(defn allSignaturesForAction
  [ODB action]
  (let [odb (filter #(= action (:op_action %)) ODB)]
    (groupTuples (mapv signature odb))))


(defn getMethodDetails [ODB action ariVer nick]
  (let [methods (filter #(and (= ariVer (:op_version %))
                              (= action (:op_action %))
                              (= nick   (:nickname %)))
                        ODB)
        _       (cond
                  (not= 1 (count methods))
                  (throw (IllegalArgumentException.
                         ("Multiple or missing methods " ariVer action nick " found:" (count methods)))))
        method (first methods)]

        method

  ))


(s/fdef generate-action-interface-methods
        :args (s/cat :methodSig (s/keys :req-un [::nickname ::parms ::responseClass])
                     :methodInAri ::odb_entry
                     :available-in (s/coll-of keyword?)
                     ))


(defn generate-action-interface-methods
  [methodSig methodInAri available-in]

  (flatten
    (let [parmNames (map :name (:parameters methodInAri))]
      (for [parameters (sgn/string-perms (:parms methodSig))]
        [
         ; normal call
         {:method  (:nickname methodSig)
          :returns (:responseClass methodSig)
          :args    (map (fn [name type] {:type type :name name}) parmNames parameters)
          :notes   (str parameters)
          }

         ;ARI callback
         {:method  (:nickname methodSig)
          :returns "void"
          :args    (map (fn [name type] {:type type :name name})
                        (conj parmNames  "callback")
                        (conj parameters (str "AriCallback<" (:responseClass methodSig) ">" )))
          :notes   (str parameters)
          }
         ]
        ))))



(defn generate-action-interfaces
  [ODB action]

  (let [interfaceTuples (allSignaturesForAction ODB action)
        method-sigs (sort-by :nickname (keys interfaceTuples))]

    (flatten
      (for [method-sig method-sigs
            :let [available-in (get interfaceTuples method-sig)
                  method-ari (getMethodDetails
                               ODB
                               action
                               (first available-in)         ; any version of ARI
                               (:nickname method-sig))]]

        (generate-action-interface-methods method-sig method-ari available-in)

        ))))


(defn generate-action-interface-class
  [ODB action]
  {
   :classname   (jg/camelName "Action" (name action))
   :isInterface true
   :package     "ch.loway.oss.ari4java.generated"
   ;:imports      a list of imports
   ;:implements   a list of implementations
   ;:extends      the class to extend
   :notes       ""
   :functions    (generate-action-interfaces ODB action)
   ;:stanzas      a list of text to implement (added after the methods)
   }

  )



(defn generate-all-action-interfaces
  [DB]
  (let [odb        (opsDb DB)
        allActions (allActions odb)]
    (map #(generate-action-interface-class odb %) allActions)))


; =================================================================
; Try everything once.


(defn process
  "Turns a database into a set of interfaces and classes."
  [database]

  (let [mif (generate-model-interfaces database)
        mci (generate-model-implementations database)
        odb (opsDb database)
        enm (generate-enum-classes odb)
        bld (generate-ari-builders database)
        aif (generate-all-action-interfaces database)



        all (-> []
                (into mif)
                (into mci)
                (into enm)
                (into bld)
                (into aif)
                )
        ]

    ;(prn aif)
    (map jg/writeOutKlass all)
    ))




; Orchestra instrumentation is active automagically
(st/instrument)
