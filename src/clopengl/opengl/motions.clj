(ns clopengl.opengl.motions
  (:require [clopengl.opengl.data.space-mutations :as data]
            [clopengl.opengl.abstract.transformation :as transform])
  (:import (org.joml Matrix4f)
           (org.lwjgl BufferUtils)))

(defmethod transform/data->opengl! :transform/matrix
  [h & _]
  (let [buffer (BufferUtils/createFloatBuffer 16)
        rx (Math/toRadians (get-in h [:rotation :x]))
        ry (Math/toRadians (get-in h [:rotation :y]))
        rz (Math/toRadians (get-in h [:rotation :z]))
        tx (get-in h [:translation :x])
        ty (get-in h [:translation :y])
        tz (get-in h [:translation :z])
        sc (:scale h)]
    (-> (Matrix4f.)
      (.translate tx ty tz)
      (.rotateXYZ rx ry rz)
      (.scale sc sc sc)
      (.get buffer))
  (assoc h :buffer buffer)))
