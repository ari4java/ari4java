(ns codegen3.methods
  (:require [clojure.data.json :as json]
            [codegen3.javagen :as javagen]
            [codegen3.signatures :as sgn])
  (:gen-class)  )


;; Il metodo di base si aspetta:
;; :path
;; :descr
;; :op     (che è una sola delle operations)


; 	{
; 		"path": "/applications/{applicationName}",
; 		"description": "Stasis application",
; 		"operations": [
; 			{
; 				"httpMethod": "GET",
; 				"summary": "Get details of an application.",
; 				"nickname": "get",
; 				"responseClass": "Application",
; 				"parameters": [
; 					{
; 						"name": "applicationName",
; 						"description": "Application's name",
; 						"paramType": "path",
; 						"required": true,
; 						"allowMultiple": false,
; 						"dataType": "string"
; 					}
; 				],
; 				"errorResponses": [
; 					{
; 						"code": 404,
; 						"reason": "Application does not exist."
; 					}
; 				]
; 			}
; 		]
; 	}


(defn mkParm
  "Dato un elemento parameters e un tipo di espansione, ritorna il
   parametro in formato {:type :name :comment}"
  [parm exp]
  (let [n (:name parm)
        t (javagen/swagger->java (:dataType parm) "")
        c (:description parm)]
    {:type t
     :name n
     :comment c}))


(defn baseSignature
  "La signature di base, senza callback"
  [path descr op]
  (let [nick (:nickname op)
        summary (str descr "\n" (:summary op))
        response (javagen/swagger->java (:responseClass op) "")
        parms (:parameters op)]
    {
      :method      nick
      :returns     response
      :isPrivate   false
      :args        (mapv #(mkParm % "") parms)
      :notes       summary
      :body        ""
      :isAbstract  true
    }))


(defn allSignsForPath
  "Per un path dato, tutte le sue signatures"
  [path-str]
  (let [path (:path path-str)
        des  (:description path-str)
        ops  (:operations path-str)]
    (map #(baseSignature path des %) ops)))


(defn javaField
  "
   Trasforma una descrizione d un field ARI in una Java.

  Esempi ARI:

   {:id {:type 'string', :description '..', :required true}}

   {:type `string`, :description `Type of bridge technology`, :required true, :allowableValues {:valueType `LIST`, :values [`mixing` `holding`]}}

   {:type `List[string]`, :description `Ids of channels participating in this bridge`, :required true}

  quelle JAva sono 
   {:type ... :name ....}

  "  

  [ariNameSymbol ariField]
  (let [atype (:type ariField)
        jtype (javagen/swagger->java atype "")
        jname (name ariNameSymbol)]  
    {:type jtype
     :name jname
     :comment "xxx"}
    ))


(defn modelFromFile
  "
  Genera un modello partendo da un descrittore.
  "
  [ariModel]
  (let [cName (:id ariModel)
        pkg   "-"
        nota  (:description ariModel)
        fieldsAri (:properties ariModel)
        fieldsJava (map #(javaField (first %) (second %) ) fieldsAri)]

    (javagen/mkDataClass pkg cName nota fieldsJava)
    
    ))


(defn allModelsForAriFile
  "
  Genera i modelli per questo file.
  
  codegen3.core> (keys  (get-in _db [:ari_1_0_0 :bridges :models :Bridge]))
  (:id :description :properties)
  codegen3.core> (keys  (get-in _db [:ari_1_0_0 :bridges :models :Bridge :properties]))
  (:id :technology :bridge_type :bridge_class :creator :name :channels)


   "
  [db ari-ver file]

  ;; VEDI SOTTO
  (let [models (get-in db [ari-ver file :models])
        modelData (map #(modelFromFile (second %)) models)
        ]
    modelData

    ))





(defn allSigsForAriFile
  "Tutte Le Signatures Per un file dato di un'ari data"
  [db ari-ver file]
  (let [file-paths (get-in db [ari-ver file :apis])]
    (flatten (mapv allSignsForPath file-paths))))


(defn- rdc-addSigToMap
  [acc sig]
  (let [k (sgn/getMethodSignature sig)]
    (assoc-in acc [k] sig)))


(defn allSigsForInterface
  "Tutte le signatures per creare un'interfaccia.
  Per calcolarle:
  - cerco le signatures per tutte le versioni note dell'ari
  - le rendo univoche sulla base della loro signature java
  Restituisco un hash la cui chiave è la sig Java ed il contenuto è
  il metodo.
  "
  [DB file ari-versions]
  (let [all-possible-sgns
        (reduce into []
            (map #(allSigsForAriFile DB % file) ari-versions))]
    (reduce rdc-addSigToMap {} all-possible-sgns)

  ))



(defn allSigsForModels
  "
  Ottiene tutte le signatures per tutti i modelli
  ricostruendole nelle varie versioni dell'ARI.
  "
  [DB ari-versions]


  )



;(def DB (readAll))
;(reduce into #{} (map #(allSigsForAriFile DB % :applications)
;                            [:ari_0_0_1 :ari_1_0_0])))









