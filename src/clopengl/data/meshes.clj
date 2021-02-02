(ns clopengl.data.meshes
 (:require [clojure.spec.alpha :as s]
           [clopengl.util.basic-specs :as _]))

(defonce bare-mesh
 {:mesh/vertices []
  :mesh/vertices.indices []})

(defonce bare-vertex
 {:vertex/coordinates []
  :vertex/color []
  :vertex/normal []
  :vertex/texture []})

(s/def
 :clgl/vertex
 (s/keys :req [:vertex/coordinates]
         :opt [:vertex/color :vertex/normal :vertex/texture]))

(s/def
 :clgl/mesh
 (s/keys :req [:mesh/vertices]
         :opt [:mesh/indices]))

(s/def :vertex/coordinates (s/and vector? :vec/size.3 :all/float))
(s/def :vertex/color (s/and vector? :vec/size.3 :all/float #(<= 0 (apply min %)) #(<= (apply max %) 1)))
(s/def :vertex/texture (s/and vector? :vec/size.2 :all/float #(<= 0 (apply min %)) #(<= (apply max %) 1)))
(s/def :vertex/normal (s/and vector? :vec/size.3 :all/float))
(s/def :mesh/vertices vector?)
(s/def :mesh/vertices.indices vector?)
