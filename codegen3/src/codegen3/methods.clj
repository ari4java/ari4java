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
        t (javagen/typeTranslator (:dataType parm) "")
        c (:description parm)]
    {:type t
     :name n
     :comment c}))


(defn baseSignature
  "La signature di base, senza callback"
  [path descr op]
  (let [nick (:nickname op)
        summary (str descr "\n" (:summary op))
        response (javagen/typeTranslator (:responseClass op) "")
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



(defn allSigsForAriFile
  "Tutte le signatures per un file dato di un'ari data"
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



;(def DB (readAll))
;(reduce into #{} (map #(allSigsForAriFile DB % :applications)
;                            [:ari_0_0_1 :ari_1_0_0])))









