(ns clopengl.data.math.camera-transformations
  ""
  (:require [clojure.spec.alpha :as s]
            [clopengl.util.basic-specs :as _]))

(defonce blank-perspective
 {:perspective/fovy nil
  :perspective/aspect nil
  :perspective/z-near nil
  :perspective/z-far nil})

(defonce blank-look-at
 {:look-at/eye nil
  :look-at/center nil
  :look-at/up nil})

(s/def
 :matrix/perspective
 (s/keys :req [:perspective/fovy :perspective/aspect :perspective/z-near :perspective/z-far]
         :opt [:perspective/buffer]))

(s/def
 :matrix/look-at
 (s/keys :req [:look-at/eye :look-at/center :look-at/up]))

(s/def :perspective/fovy float?)
(s/def :perspective/aspect float?)
(s/def :perspective/z-near float?)
(s/def :perspective/z-far float?)
(s/def :look-at/eye vector?)
(s/def :look-at/center vector?)
(s/def :look-at/up vector?)
;; (s/def :look-at/eye :vec3d/float)
;; (s/def :look-at/center :vec3d/float)
;; (s/def :look-at/up :vec3d/float)
