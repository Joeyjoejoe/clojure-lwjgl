(ns clopengl.data.stage
 (:require [clojure.spec.alpha :as s]
           [clopengl.util.basic-specs :as _]))

(defonce bare-stage
 {:id
  :entities})

(defonce bare-entity
 {:id nil
  :position {:initial [] :3d-transform nil}
  :model nil
  :program nil
  :render nil})

(defonce hero
 (-> bare-entity
    (+name "hero")
    (+model "path/to/model/file")
    (+program "program-name-or-id?")))

(defonce hero
 {:name "hero"
  :model "path/to/model/file"
  :program "program-name-or-id?"
  :position []
  :movement nil
  :render nil
  })
