(ns clopengl.interpret.math.3D-transformations
  (:import (org.joml Matrix4f)
           (org.lwjgl BufferUtils))
  (:require [clopengl.interpret.interface :as interface]))

(defmethod interface/data->opengl! :matrix/translation
  ([_ h]
   (let [tx (:translate/x h)
         ty (:translate/y h)
         tz (:translate/z h)]
     (-> (Matrix4f.)
         (.translate tx ty tz))))
  ([t h buff?]
   (let [mat (interface/data->opengl! t h)]
     (cond
       (not buff?) mat
       :else (let [buffer (BufferUtils/createFloatBuffer 16)]
               (-> mat
                   (.get buffer)))))))

(defmethod interface/data->opengl! :3d/tranform
  [_ h & _]
  (let [buffer (BufferUtils/createFloatBuffer 16)
        rx (Math/toRadians (or (get-in h [:matrix/rotation :rotate/x]) 0.0))
        ry (Math/toRadians (or (get-in h [:matrix/rotation :rotate/y]) 0.0))
        rz (Math/toRadians (or (get-in h [:matrix/rotation :rotate/z]) 0.0))
        tx (or (get-in h [:matrix/translation :translate/x]) 0.0)
        ty (or (get-in h [:matrix/translation :translate/y]) 0.0)
        tz (or (get-in h [:matrix/translation :translate/z]) 0.0)
        sx (or (get-in h [:matrix/scalation :scale/x]) 1.0)
        sy (or (get-in h [:matrix/scalation :scale/y]) 1.0)
        sz (or (get-in h [:matrix/scalation :scale/z]) 1.0)]
    (-> (Matrix4f.)
      (.translate tx ty tz)
      (.rotateXYZ rx ry rz)
      (.scale sx sy sz)
      (.get buffer))
  (assoc h :buffer buffer)))
