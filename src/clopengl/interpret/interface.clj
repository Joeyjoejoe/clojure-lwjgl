(ns clopengl.interpret.interface
  "Interface definition for interpreting data."
  (:require [clojure.spec.alpha :as s]
            [clojure.pprint :as pp]))

(defmulti data->opengl!
  (fn [t data & _]
    (if (s/valid? t data)
      t
      :invalid)))

(defmethod data->opengl! :invalid [t h & opts] (pp/pprint (s/explain-data t h)))
