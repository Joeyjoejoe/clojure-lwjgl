(ns clopengl.interpret.math.camera-transformations
  (:import (org.joml Matrix4f)
           (org.lwjgl BufferUtils))
  (:require [clopengl.interpret.interface :as interface]))

(defmethod interface/data->opengl! :matrix/perspective
  [_ h & _]
  (let [buffer (BufferUtils/createFloatBuffer 16)
        f  (:perspective/fovy h)
        a  (:perspective/aspect h)
        zn (:perspective/z-near h)
        zf (:perspective/z-far h)]
    (-> (Matrix4f.)
        (.perspective f a zn zf)
        (.get buffer))
    (assoc h :buffer buffer)))

(defmethod interface/data->opengl! :matrix/look-at
  [_ h & _]
  (let [buffer (BufferUtils/createFloatBuffer 16)
        [ex ey ez] (:look-at/eye h)
        [cx cy cz] (:look-at/center h)
        [ux uy uz] (:look-at/up h)]
    (-> (Matrix4f.)
        (.lookAt ex ey ez (+ ex cx) (+ ey cy) (+ ez cz) ux uy uz)
        (.get buffer))
      (assoc h :buffer buffer)))
