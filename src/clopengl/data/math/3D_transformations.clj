(ns clopengl.data.math.3D-transformations
  ""
  (:require [clojure.spec.alpha :as s]
            [clopengl.util.basic-specs :as _]))

(defonce blank-3d-transform
 {:matrix/rotation nil
  :matrix/translation nil
  :matrix/scalation nil})

(defonce blank-rotation
 {:rotate/x 0.0
  :rotate/y 0.0
  :rotate/z 0.0})

(defonce blank-translation
 {:translate/x 0.0
  :translate/y 0.0
  :translate/z 0.0})

(defonce blank-scalation
 {:scale/x nil
  :scale/y nil
  :scale/z nil})

(s/def
 :3d/tranform
 (s/keys :req [(or :matrix/rotation :matrix/translation :matrix/scalation)]
         :opt [:matrix/rotation :matrix/translation :matrix/scalation]))

(s/def
 :matrix/rotation
 (s/nilable (s/keys :req [(or :rotate/x :rotate/y :rotate/z)]
                    :opt [:rotate/x :rotate/y :rotate/z])))

(s/def
 :matrix/translation
 (s/nilable (s/keys :req [(or :translate/x :translate/y :translate/z)]
                    :opt [:translate/x :translate/y :translate/z])))

(s/def
 :matrix/scalation
 (s/nilable (s/keys :req [(or :scale/x :scale/y :scale/z)]
                    :opt [:scale/x :scale/y :scale/z])))

(s/def :rotate/x :clopengl/coord)
(s/def :rotate/y :clopengl/coord)
(s/def :rotate/z :clopengl/coord)
(s/def :translate/x :clopengl/coord)
(s/def :translate/y :clopengl/coord)
(s/def :translate/z :clopengl/coord)
(s/def :scale/x :clopengl/coord)
(s/def :scale/y :clopengl/coord)
(s/def :scale/z :clopengl/coord)
