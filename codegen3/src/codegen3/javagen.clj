(ns codegen3.javagen
  (:require [clojure.data.json :as json]
            [clojure.string :as str]
            [orchestra.spec.test :as orchestra]
            [clojure.spec.alpha :as s]))

;(def BASE-CLASS-PATH "./gen-java")
(def BASE-CLASS-PATH "../classes")

(def PREAMBLE
  (str " - (clj) THIS CLASS IS AUTOMATICALLY GENERATED - \n"
       " - -    PLEASE DO NOT EDIT MANUALLY    - - \n\n"
       " Generated on: " (.toString (java.util.Date.))
       "\n\n"))



;ch.loway.oss.ari4java.generated
;(def BASE-GEN-PKG "gg")
(def BASE-GEN-PKG "ch.loway.oss.ari4java.generated")

(def IMPORTS-INTERFACE [
                        "java.util.Date"
                        "java.util.List"
                        "java.util.Map"
                        "java.util.ArrayList"
                        "ch.loway.oss.ari4java.tools.RestException"
                        "ch.loway.oss.ari4java.tools.AriCallback"
                        "ch.loway.oss.ari4java.tools.tags.*"])



;(genclassfile {:package "a.b.c"
;               :classname "Pippon"
;               :imports ["java.w"]
;               :implements ["tool", "tool2"]
;               :extends "base"
;               :functions [
;                  { :method "pippo"
;                    :returns "void"
;                    :args [ {:type "int" :name "pippo
;                            {:type "String" :name "plut"}]
;                    :notes ["yoo"]}
;               ]
;               :stanzas ["m;" ]  } )


; ============= SPECS ============

(defn existing-string? [s]
  (and (string? s)
         (pos? (count s))))

(defn optional-bool? [b]
  (or nil? boolean?))

(defn optional-existing-string? [s]
  (or nil? existing-string?))


(s/def ::attr (s/and #(existing-string? (:name %))
                     #(existing-string? (:type %))))


(s/def ::method existing-string?)
(s/def ::returns existing-string?)
(s/def ::isPrivate boolean?)
(s/def ::args (s/coll-of ::attr))
(s/def ::notes string?)
(s/def ::body string?)
(s/def ::isAbstract boolean?)
(s/def ::annotation string?)
(s/def ::throws (s/coll-of existing-string?))

(s/def ::methodBody
  (s/keys :req-un [::method ::returns ::isPrivate ::args ::notes
                   ::body ::isAbstract ]
          :opt-un [::annotation ::throws]))


(s/def ::classname existing-string?)
(s/def ::isInterface boolean?)
(s/def ::isEnum boolean?)
(s/def ::package   existing-string?)
(s/def ::imports  (s/coll-of existing-string?))
(s/def ::implements (s/coll-of existing-string?))
(s/def ::extends  existing-string?)

(s/def ::functions  (s/coll-of ::methodBody))

(s/def ::stanza existing-string?)
(s/def ::stanzas  (s/coll-of ::stanza))

(s/def ::classFile
  (s/keys :req-un [::classname ::isInterface
                   ::package ::imports
                   ::notes ::functions]
          :opt-un [::isEnum ::implements ::extends ::stanzas]

          ))


; =================================

(defn camelNameInner
  "Creates a camelName - es getDate by joining 'get' (prefix)
  and 'date' (value)"
  [p v fnTrasformer]
  {:pre [(pos? (count v))]}
  (str p
       (fnTrasformer (subs v 0 1))
       (subs v 1)))

(s/fdef
  camelNameInner
  :args (s/cat :p string?
               :v existing-string?
               :fnTrasformer ifn?)
  :ret string?)


(defn camelName
  "Creates a camelName - es getDate by joining 'get' (prefix)
  and 'date' (value)"
  [p v]
  (camelNameInner p v str/upper-case))

(s/fdef
  camelName
  :args (s/cat :p string?
               :v (s/and string? #(-> % count pos?))))




(defn className
  "A class' name is uppecased"
  [c]
  (camelName "" c))

(s/fdef
  className
  :args (s/cat :c (s/and string? #(-> % count pos?))))



(defn lcName [c]
  (camelNameInner "" c str/lower-case))

(s/fdef
  lcName
  :args (s/cat :c (s/and string? #(-> % count pos?))))


(defn mkSection
  "Crea una sezione con prefisso e suffisso.
   Items puo' essere:
    - nil o vettore vuoto: non fa nulla
    - uno scalare: aggiunge solo una volta
    - un vettore: aggiunge n volte"
  [itemprefix itempostfix items]
  (let [i2 (cond
             (string? items) [items]
             :else (vec items))]
    (reduce
      #(str %1 itemprefix %2 itempostfix)
      ""
      i2)))

(s/fdef
  mkSection
  :args (s/cat :itemprefix string?
               :itempostfix string?
               :items (s/or :nil    nil?
                            :onestr string?
                            :manystr (s/coll-of string?)))
  :ret string?
  )




(defn mkImports
  "Creates import section."
  [lImports]
  (mkSection "import " ";\n" lImports))

(s/fdef
  mkImports
  :args (s/cat :lImports (s/coll-of string?)))


(defn indent-prefix
  "Adds a given prefix to a block of text (string).
  Returns the string with prefix applied. "
  [prefix text]
  (cond
    (nil? text)  (indent-prefix prefix "")
    :else (str/join "\n"
            (map #(str prefix %) (str/split-lines text))))

    )

(s/fdef
  indent-prefix
  :args (s/cat :prefix string?
               :text   string?)
  :ret string?)


(defn indent
  "Indents a text by three spaces"
  [text]
  (indent-prefix "   " text))

(s/fdef
  indent
  :args (s/cat :text string?))


(defn mkComment
  "Creates a Java comment.
  Receives one string of (multi-line) text."
  [text]
  (cond
    (nil? text) (mkComment "")
    :else (let [STARS "************************************************"]
            (str "/" STARS "\n"
                 (indent-prefix " * " text) "\n"
                 " " STARS "/" "\n\n"
                 ))))

(s/fdef
  mkComment
  :args (s/cat :text string?))


(defn genAttr
  "Generate an attribute. Checks that :type and :name are defined."
  [{t :type n :name}]
  (str t " " n))

(s/fdef
  genAttr
  :args (s/cat :input ::attr))

(defn genAttrs
  "The attributes for a method.
   data is a list of attributes."
  [data]
  (let [signatures (map genAttr data)]
    (str/join ", " signatures)))

(s/fdef
  genAttrs
  :args (s/cat :data (s/coll-of ::attr)))

(defn genMethodBody
  "Creates a Java method
      :method
      :returns
      :isPrivate   if undefined, public
      :args        [{:type :name}]
      :notes
      :body
      :isAbstract
      :annotation
      :throws [eccezioni]
  "
  [data]
  (let [{:keys [method returns isPrivate args notes body isAbstract annotation throws]} data]
    (str
      "\n\n"
      (mkComment notes)
      (if annotation (str annotation "\n") "")
      (if isPrivate "private " "public ")
      returns " "
      method "("
      (genAttrs args)
      ")"
      ; throws
      (if (pos? (count throws))
        (str " throws "
             (str/join ", " throws))
        ""
        )

      (if isAbstract
        ";"
        (str
          "{\n"
          (indent body)
          "\n"
          "}"))
      "\n"

      )))


(s/fdef
  genMethodBody
  :args (s/cat :data ::methodBody)
  :ret existing-string?
  )




(defn genFilename
  "Returns the relative filename of a package + class"
  [pkgName kName]
  (let [pkg (str/replace pkgName "." "/")]
    (str pkg "/" kName ".java")))


(s/fdef
  genFilename
  :args (s/cat :pkgName existing-string?
               :kName existing-string?))



(defn genClassFile
  "
  Given a data structure that describes
  our own class, creates a text file containing
  the class itself and a proposed text file.


  Inputs
       :classname    the class name
       :isInterface
       :isEnum
       :package      the package name
       :imports      a list of imports
       :implements   a list of implementations
       :extends      the class to extend
       :notes        the class comments (version etc)
       :functions    a list of methods to implement (may be empty)
       :stanzas      a list of text to implement (added after the methods)

   Output
       :body         the textual class in Java
       :filename     the file name to store it to
  "
  [data]
  (let [{:keys [classname isInterface isEnum package imports implements extends notes functions stanzas]} data]
    {:filename (genFilename package classname)
     :body
               (str
                 "package " package ";\n\n"
                 (mkImports imports)
                 "\n"
                 (mkComment (str PREAMBLE notes))
                 "public "
                 (cond
                   isInterface " interface "
                   isEnum " enum  "
                   :else " class ")

                 classname
                 (mkSection " extends " "" extends)
                 (mkSection " implements " " " implements)

                 " {\n"

                 (mkSection "" "\n\n" stanzas)
                 (mkSection "" "\n\n" (map #(indent (genMethodBody %)) functions))

                 "}\n\n")
     }
    ))

(s/fdef
  genClassFile
  :args (s/cat :data ::classFile)
  :ret (s/and #(existing-string? (:filename %))
                    #(existing-string? (:body %)))
  )


(def knownSwaggerTypes
  "These Swagger types match a Java type directly."

  {
   "string"     "String",
   "long"       "long",
   "int"        "int",
   "double"     "double",
   "date"       "Date",
   "object"     "String",
   "boolean"    "boolean",
   "containers" "Map<String,String>"
   })



(defn swagger->java
  "Turns a Swagger type into a matching Java type.
  We need to know if we are asking for a concrete implementation
  or an interface (as for native objects this is different)
  so we set the API version when we require a concrete
  implementation.
  "
  [sw-type api-version]
  (let [inList (get (re-matches #"^List\[(.+)\]$" sw-type) 1)
        known (knownSwaggerTypes sw-type)
        concrete? (pos? (count api-version))]
    (cond
      inList (str "List<" (swagger->java inList api-version) ">")
      known known
      :else (if concrete?
              (str sw-type "_impl_" api-version)
              sw-type))))

;; DATA CLASSES
;; Only containers for data.


(defn notImplemented
  []
  (str "throw new IllegalArgumentException(\"This version of ARI does not support this field\");"))

(defn mkSetterTypeAnnotation
  "If type is List<XXX>, Jackson needs a type hint
  to deserialize elements because it does not see
  the elided signature.

  @JsonDeserialize( contentAs=String.class )
  public void setBridge_ids(List<String> val )


  "

  [t]
  (let [inList (get (re-matches #"^List<(.+)>$" t) 1)]
    (cond
      inList (str "@JsonDeserialize( contentAs=" inList ".class )")
      :else ""

      )))


(defn mkGetterVal [field]
  (let [{t :type v :name c :comment ni :notimplemented io :interfaceonly} field]
    {
     :method     (camelName "get" v)
     :returns    t
     :args       []
     :notes      (str "get " v "\n" c)
     :isAbstract (= true io)
     :body       (cond
                   (true? io)
                   ""
                   (true? ni)
                   (notImplemented)
                   :else
                   (str "return this." v ";"))
     }))

(s/fdef
  mkGetterVal
  :args (s/cat :field existing-string?)
  :ret  ::methodBody)


(defn mkSetterVal [field]
  (let [{t :type v :name c :comment ni :notimplemented io :interfaceonly} field]
    {
     :method     (camelName "set" v)
     :returns    "void"
     :args       [field]
     :annotation (cond
                   (true? io) nil
                   :else (mkSetterTypeAnnotation t))
     :notes      (str "sets " v "\n" c)
     :isAbstract (= true io)
     :body       (cond
                   (true? io)
                   ""
                   (true? ni)
                   (notImplemented)
                   :else
                   (str "this." v " = " v ";"))
     }))

(s/fdef
  mkSetterVal
  :args (s/cat :field existing-string?)
  :ret  ::methodBody)


(defn mkPrivateField [acc field]
  (let [{t :type v :name} field]

    (conj acc
          (str "private " t " " v ";"))
    ))


(defn mkGettersSetters
  "da usare con reduce"
  [funcs field]
  (conj funcs
        (mkGetterVal field)
        (mkSetterVal field))
  )


(defn mkCommonDataClass
  [package klass extends implements imports notes]
  {
   :classname  klass
   :package    package
   :extends    extends
   :implements implements
   :imports    imports
   :notes      notes
   })

(s/fdef
  mkGetterVal
  :args (s/cat :package existing-string?
               :classname existing-string?
               :extends string?
               :implements string?
               :imports (s/coll-of string?)
               :notes string?)
  :ret  ::classFile)



(defn sortFields
  "We keep fields in a sorted order so they
  don't bounce around in different builds."
  [lfields]
  (sort-by :name lfields))


(defn mkDataClassInterface
  [package klass extends implements imports notes lfields]
  (into
    (mkCommonDataClass package klass extends implements imports notes)
    {
     :isInterface true
     :functions   (reduce mkGettersSetters [] (sortFields lfields))
     }))


(defn mkDataClass
  "
  (mkDataClass p c n
             [{:type int :name pluto}
              {:type String :name pippo}]
             )"

  [package klass extends implements imports notes lfields]
  (into
    (mkCommonDataClass package klass extends implements imports notes)


    {
     :functions (reduce mkGettersSetters [] (sortFields lfields))
     :stanzas   (reduce mkPrivateField [] (sortFields lfields))
     }))


(defn mkDataClassForModelClass
  "Crea una data class"
  [{:keys [id properties]}]
  (let [newProps (map (fn [[k v]]
                        [:name (str k)
                         :type (:type v)])
                      properties)]



    )




  )


(defn mkEnumEntry
  "Creates an enum entry.
   e.g. 'hold' -> HOLD('hold')
        '*'    -> STAR('*')

  The title is uppercased
  and * and # are translated.
  "
  [v]

  (let [t (-> v
              (str/replace "*" "STAR")
              (str/replace "#" "POUND")
              (str/upper-case))]
    (str " " t "(\"" v "\") ")))



(defn mkEnum
  [package klass notes values]

  {
   :isEnum    true
   :classname (className klass)
   :package   package
   :notes     notes
   :stanzas   [(str/join ",\n " (map mkEnumEntry (sort values)))
               ";"
               "public final String value;"
               (str "private " (className klass) "(String v) { this.value = v; }")
               "public String toString() { return this.value; }"

               ]
   }

  )



(defn writeOutKlass
  "Generates and writes a classfile on disk.
   Returns the filename."
  [klass]
  (let [{:keys [filename body]} (genClassFile klass)
        realPath (str BASE-CLASS-PATH "/" filename)]
    (clojure.java.io/make-parents realPath)
    (spit realPath body)
    realPath))

(s/fdef
  writeOutKlass
  :args (s/cat :klass ::classFile)
  :ret  string?)



(defn writeInterface
  "Una interfaccia è scritta su disco 
  sulla base del nome del file e dei metodi"
  [file comments meths]
  (let [klass {
               :classname   (camelName "Action" (name file))
               :isInterface true
               :package     BASE-GEN-PKG
               :imports     IMPORTS-INTERFACE
               ;:implements   ""
               ;:extends      the class to extend
               :notes       comments
               :functions   meths
               ;:stanzas      a list of text to implement (added after the methods)
               }]
    (writeOutKlass klass)))






;(writeOutKlass
;   (mkDataClass "p.k.g" "c" "no"
;      [{:type "int" :name "pluto"} {:type "String" :name "pippno"}]  ))




















;; CREATE FILE
;; (let [file-name "path/to/whatever.txt"]
;;  (make-parents file-name)
;;  (spit file-name "whatever"))


(orchestra/instrument)