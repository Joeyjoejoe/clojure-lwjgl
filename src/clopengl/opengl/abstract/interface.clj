(ns clopengl.opengl.abstract.interface
  "Interface definition for interpreting data."
  (:require [clojure.spec.alpha :as s]))

(defmulti data->opengl!
  (fn [t data & _]
    (if (s/valid? t data)
      t
      :invalid)))

(defmethod data->opengl! :invalid [t h & opts] (clojure.pprint/pprint (s/explain-data t h)))
