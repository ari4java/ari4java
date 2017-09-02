(ns codegen3.core
  (:require [clojure.data.json :as json])
  (:require [codegen3.methods :as meth])
  (:require [codegen3.javagen :as javagen])
  (:require [com.rpl.specter :as spk])
  (:gen-class))

;; Load func: cmd+enter
;; Load all:  shift+cmd+enter

;; TBD
;; - check signatures using :pre and  :post

;; WHAT WE HAVE TO BUILD
;; I would
;; - Read all AMI versions     [*]
;; - Generate Java signatures
;; - Merge all signatures -> Interfaces
;; - Write interfaces
;; - Write each specific object

;; ## INTERFACES

;; ### ActionXXXX
;; e.g. ActionApplication.java

;; ### AriBuilder

;; ### Oggetti
;; es Dialed- vuoto
;;

;; ### Eventi
;; es DEvicestate extends EventSource

;; ## IMPLEMENTATION
;; ### Action
;; ### AriBuilder
;; ### Oggetti









(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hellox, World!"))

;; ALL KNOWN ARI VERSIONS
(def ARI_VERSIONS
  ["ari_0_0_1"
   "ari_1_0_0"])


(def ARI-VERSIONS-KW
  (vec (map keyword ARI_VERSIONS)))

;; ALL KNOWN ARI FILES
(def ALL_FILES
  {"applications" {},
;   "asterisk"     {},
;   "bridges"      {},
;   "channels"     {},
;   "deviceStates" {},
   "events"       {}
   })




(def ALL-FILES-KW
  (vec (map keyword (keys ALL_FILES))))

;; READ ARI FOLDER as JSON
(defn loadResourceFile
  "Loads a resource file as plain text; if missing, 
   returns the default contents"
  [resource-path default-if-missing]
  (let [resource (clojure.java.io/resource resource-path)
        contents (cond
                   (nil? resource) default-if-missing
                   :else (slurp resource))]
    contents))


(defn readJson
  "Loads a JSON file as a Clojure structure.
  e.g.
    (readJson \"ari_0_0_1\" \"applications\")
  "
  [ariVersion filename]
  (let [file (str ariVersion "/" filename ".json")
        data (loadResourceFile file "{}")]
    (json/read-str data :key-fn keyword)))

(defn readJsonAsStruct
  "Legge un file json come
    {:ari {:filename {...data...}}}"
  [ariVersion filename]
  (let [kVer (keyword ariVersion)
        kFnam (keyword filename)
        data (readJson ariVersion filename)]

    {kVer {kFnam data}}))



(defn readKnownFilesForVersion
  "Loads all known files for a given version"
  [acc ariVersion]
  (let [files (keys ALL_FILES)
        redLoadFile (fn [acc filename]
                      (assoc-in acc [(keyword ariVersion)
                                     (keyword filename)]
                                (readJson ariVersion filename)))]
    (reduce redLoadFile acc files)))


(defn readAll
  "Reads all files for all known Asterisk versions"
  []
  (reduce readKnownFilesForVersion {} ARI_VERSIONS))


(defn allFilesAllAris
  "Ritorna tuple di tutte le permutazioni di file e di tutte le ARI,
   anche quelle che non esistono"
  []
  (for [ari ARI_VERSIONS
        file (keys ALL_FILES)]
    [(keyword ari) (keyword file)]))


(defn mkFnSignature
  "Creates a list of signatures for each file"
  [apiOp]

  )



(defn write$interfaces [DB]
  (for [file ALL-FILES-KW]
    (javagen/writeInterface
      file
      "--comment--"
      (vals (meth/allSigsForInterface DB file ARI-VERSIONS-KW)))))

(defn write$models [DB]
  (for [file ALL-FILES-KW]
    1

    )

  )



(defn run$
  []
  (let [DB (readAll)]
    (do
      (write$interfaces DB))))





;; How is this supposed to be working?
;; 1. we read the contents of known files in all
;;    ari interface bindings. So we have all we need.
;; 2. we build the interface definitions. These are obtained by
;;    joiing together all forms of all methods.
;; 3. we build events
;; 4. we build ???
;; 5. for each version of the interface, we build ...


;; READ ALL METHODS
;; (def DT (readAll))
;; apis is an array
;;  (get-in DT [:ari_1_0_0 :asterisk :apis 0 :operations])

;; READS ALL MODELS
;;  (keys (get-in DT [:ari_1_0_0 :asterisk :models]))
;; a specific model
;;  (get-in DT [:ari_1_0_0 :asterisk :models :BuildInfo])




;; SVILUPPO
