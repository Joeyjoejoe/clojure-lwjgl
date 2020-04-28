(ns clopengl.opengl.abstract.transformation
  "Interface definition for transforming data.")

(defmulti data->opengl!
  (fn [data & opts]
    (:type data)))

;; Would it be usefull?
;; (defmulti data->record
;;   (fn [data]
;;     (:type data)))
