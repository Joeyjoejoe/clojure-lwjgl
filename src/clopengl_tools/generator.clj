(ns clopengl-tools.generator
  (:require [clojure.java.io :as io]
            [clojure.term.colors :as color]))

(defonce root (System/getProperty "user.dir"))

(defn report-create [msg]
  (println (str "  " (color/green (char 10004)) (color/green "  create ") (color/bold msg))))

(defn report-exists [msg]
  (println (str "  " (color/red (char 10008)) (color/yellow "  exists ") (color/grey msg))))

(defn -create-dirs [dirnames t]
  (doseq [p (reductions #(str %1 "/" %2) dirnames)]
    (let [path (str root "/src/clopengl/" t "/" p)]
      (if (not (.exists (io/file path)))
        (do
          (report-create path)
          (.mkdir (java.io.File. path)))
        (report-exists path)))))

(defn -str->path [s]
  (-> s
      (clojure.string/replace #"-" "_")
      (clojure.string/replace #"\." "/")))

(defn -str->ns [s]
  (-> s
      (clojure.string/replace #"_" "-")
      (clojure.string/replace #"/" ".")))

(defn -write-file-lines [path lines]
  (if (not (.exists (io/file path)))
    (do
      (with-open [w (io/writer path)]
        (doseq [i lines]
          (.write w i)
          (.newLine w)))
      (report-create path))
    (report-exists path)))

(defn -generate-data-file [p s]
  (let [path       (str root "/src/clopengl/data/" p ".clj")
        ns         (str "(ns clopengl.data." s)
        req        (str "  (:require [clojure.spec.alpha :as s]))")
        blank-fn   (str "(defonce blank-" s)
        blank-body (str "  {})")
        type-spec  (str "(s/def :clopengl/" s " true)")
        lines      [ns req "" blank-fn blank-body "" type-spec]]
    (-write-file-lines path lines)))

(defn -generate-interpret-file [p s]
  (let [path   (str root "/src/clopengl/interpret/" p ".clj")
        ns     (str "(ns clopengl.interpret." s)
        req    (str "  (:require [clopengl.interpret.interface :as interface]))")
        m-meth (str "(defmethod interface/data->opengl! :clopengl/" s)
        m-args (str "  [_ _ & _]")
        m-body (str ";; Implement me")
        m-end  (str ")")
        lines  [ns req "" m-meth m-args m-body m-end]]
    (-write-file-lines path lines)))

(defn entity
  "Generate necessary entity files in `src/clopengl/`. These files are:
  `data/s.clj`, `interpet/s.clj`"
  [s]
  (let [path   (-str->path s)
        new-ns (-str->ns s)
        dirs (butlast (clojure.string/split path #"/"))]
  (if (not (nil? dirs))
    (do (-create-dirs dirs "data")
        (-create-dirs dirs "interpret")))
  (-generate-data-file path new-ns)
  (-generate-interpret-file path new-ns)))

(defn -main
  "Dispatch `s` to the generator function named `kind`"
  [kind s]
  (if-let [f (resolve (symbol (str "clopengl-tools.generator/" kind)))]
    (f s)
    (println (str "'" kind "'" " is not a valid generator type"))))
