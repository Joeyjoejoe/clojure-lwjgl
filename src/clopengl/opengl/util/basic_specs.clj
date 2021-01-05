(ns clopengl.opengl.util.basic-specs
  (:require [clojure.spec.alpha :as s]))

(s/def :clopengl/id int?)
(s/def :clopengl/path string?)
(s/def :clopengl/coord (s/nilable float?))
