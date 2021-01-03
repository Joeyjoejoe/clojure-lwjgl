(ns clopengl.opengl.abstract.interface
  "Interface definition for interpreting data.")

(defmulti data->opengl!
  (fn [data & opts]
    (:type data)))

;; Would it be usefull?
;; (defmulti data->record
;;   (fn [data]
;;     (:type data)))
