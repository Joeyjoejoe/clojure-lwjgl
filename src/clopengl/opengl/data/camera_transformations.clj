(ns clopengl.opengl.data.camera-transformations
  ""
  (:require [clojure.spec.alpha :as s]
            [clopengl.opengl.util.basic-specs :as _]))

(s/def :matrix/perspective (s/keys :req [:perspective/fovy
                                         :perspective/aspect
                                         :perspective/z-near
                                         :perspective/z-far]
                                   :opt [:perspective/buffer]))
(defonce blank-perspective {
  :perspective/fovy nil
  :perspective/aspect nil
  :perspective/z-near nil
  :perspective/z-far nil
})

(s/def :matrix/look-at (s/keys :req [:look-at/eye :look-at/center :look-at/up]))

(defonce blank-look-at {
  :look-at/eye nil
  :look-at/center nil
  :look-at/up nil
})

(s/def :perspective/fovy float?)
(s/def :perspective/aspect float?)
(s/def :perspective/z-near float?)
(s/def :perspective/z-far float?)
(s/def :look-at/eye :float/vec3)
(s/def :look-at/center :float/vec3)
(s/def :look-at/up :float/vec3)
