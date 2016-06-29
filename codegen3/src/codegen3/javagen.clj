(ns codegen3.javagen
  (:require [clojure.data.json :as json])
  (:require [clojure.string :as s])
  (:gen-class))


;(genclassfile {:package "a.b.c"
;               :classname "Pippon"
;               :imports ["java.w"]
;               :implements ["tool", "tool2"]
;               :extends "base"
;               :functions [
;                  { :method "pippo"
;                    :returns "void"
;                    :args [ {:type "int" :name "pippo"}
;                            {:type "String" :name "plut"}]
;                    :notes ["yoo"]}
;               ]
;               :stanzas ["m;" ]  } )


(defn camelName
  "Creates a camelName - es getDate by joining 'get' (prefix)
  and 'date' (value)"
  [p v]
  (str p
       (s/upper-case (subs v 0 1))
       (subs v 1)))


(defn mkSection
  "Crea una sezione con prefisso e suffisso.
   Items puo' essere:
    - nil o vettore vuoto: non fa nulla
    - uno scalare: aggiunge solo una volta
    - un vettore: aggiunge n volte"
  [itemprefix itempostfix items]
  (let [i2 (cond
              (string? items) [ items ]
              :else           (vec items))]
    (reduce
      #(str %1 itemprefix %2 itempostfix) ""
      i2 )))


(defn mkImports
  "Creates import section."
  [lImports]
  (mkSection "import " ";\n" lImports) )

(defn mkComment
  "Creates a Java comment"
  [lines]
  (str "/******\n"
      (mkSection " * " "\n" lines)
       "****/\n\n"))



(defn genAttrs
  "The attributes for a method."
  [data]
  (let [signatures (map #(str (:type %) " " (:name %)) data)]
    (s/join ", " signatures)))


(defn genbody
  "Creates a Java method
      :method
      :returns
      :isPrivate   if undefined, public
      :args        [{:type :name}]
      :notes
      :body
  "
  [data]
  (let [{:keys [method returns isPrivate args notes body]} data]
  (str
    (mkComment notes)
    (if isPrivate "private " "public ")
    returns " "
    method "("
    (genAttrs args)
    "){\n"
     body

    "}"




   )))




(defn genclassfile
   "Inputs
        :classname    the class name
        :isInterface
        :package      the package name
        :imports      a list of imports
        :implements   a list of implementations
        :extends      the class to extend
        :notes        the class comments (version etc)
        :functions      a list of methods to implement (may be empty)
        :stanzas      a list of text to implement (added after the methods)

    Output
        :body
        :filename
   "
   [data]
   (let [{:keys [classname isInterface package imports implements extends notes functions stanzas]} data]
   { :filename "x"
     :body
       (str
          "package " package ";\n\n"
          (mkImports imports)
          (mkComment notes)
          "public "
               (if isInterface "interface " "class ")
               classname
               (mkSection " extends " "" extends)
               " implements "
               (mkSection "" ", " implements)
               "java.io.Serializable "
               " {\n"

          (mkSection "" "\n\n" (map genbody functions))
          (mkSection "" "\n\n" stanzas)

          "}\n\n")
    }
  ))


