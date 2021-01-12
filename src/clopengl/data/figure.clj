(ns clopengl.data.figure
 (:require [clojure.spec.alpha :as s]
           [clopengl.util.basic-specs :as _]))

(defonce bare-figure
 {:vertices []
  :indices  []})

(defonce bare-vertex
 {:vertex/coord []
  :vertex/color.rgb []
  :vertex/texture.xy []})

;; (s/def :vertex/coord.xyz :clopengl/coord)
;; (s/def :vertex/color.rgb :clopengl/coord)
