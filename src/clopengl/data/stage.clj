(ns clopengl.data.stage
 (:require [clojure.spec.alpha :as s]
           [clopengl.util.basic-specs :as _]))

(defonce bare-stage
 {:id
  :entities})

(defonce bare-entity
 {:id nil
  :position {:initial [x y z] :3d-transform nil}
  :model nil
  :program nil})
