(ns codegen3.ari4java
  (:require [codegen3.javagen :as jg]))

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


(defn models-for
  "A list of models for an ARI version.
  Models are held in:
    -> DB :ari_1_0_0 :applications :models
    -> DB :ari_1_0_0 :events :models

  Returns a map of models indexed by  model name.
  "
  [DB version]
  (let [am (-> DB version :applications :models)
        em (-> DB version :events       :models)
        ]

    (into am em)))

(defn model-names-for
  [DB version]
  (keys (models-for DB version)))


(defn all-ari-versions [db]
  (keys db))


(defn all-possible-model-names
  "Returns a list of all possible model names across all ARI versions"
  [DB]
  (reduce into #{}
          (map (partial model-names-for DB )
               (all-ari-versions DB))))


(defn get-model [DB version name]
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

(defn merge-property
  "Derives an annotated property from an existing annotated
  property p1 + a non-annotated property and a version.
  An annotated property has a list of versions it applies to.
  "

  [ap1 p2 name version]

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
)
)


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

                   ]

    (reduce (fn [a v] (merge-property a (:prop v) prop (:ver v)))
            nil
            allprops)
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
  (jg/mkDataClassInterface
    "ch.loway.oss.ari4java.generated"
    (name (:model model))
    nil ;; extends
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

      )))


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





;; ACTIONS




(defn process
  "Turns a database into a set of interfaces and classes."
  [database]

  (let [mif (generate-model-interfaces database)
        mci (generate-model-implementations database)

        all (-> []
                (into mif)
                (into mci))
        ]


    (map jg/writeOutKlass all)


    )


  )