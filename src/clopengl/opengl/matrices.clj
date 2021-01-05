(ns clopengl.opengl.matrices
  (:require [clopengl.opengl.data.3D-transformations :as data]
            [clopengl.opengl.abstract.interface :as interface])
  (:import (org.joml Matrix4f)
           (org.lwjgl BufferUtils)))


(defonce build-3d-transform data/blank-3d-transform)

(defn +rotate
  ([axis angle]
   (let [k (keyword "rotate" (name axis))]
     (-> data/blank-rotation
       (update k + angle))))
  ([t3d axis angle]
   (let [r  (+rotate axis angle)
         rr (merge-with + (:matrix/rotation t3d) r)]
     (assoc t3d :matrix/rotation rr))))

(defn +translate
  ([axis value]
   (let [k (keyword "translate" (name axis))]
     (-> data/blank-translation
       (update k + value))))
  ([t3d axis value]
   (let [t  (+translate axis value)
         tt (merge-with + (:matrix/translation t3d) t)]
     (assoc t3d :matrix/translation tt))))

(defn +scale
  ([axis value]
   (let [k (keyword "scale" (name axis))]
     (-> data/blank-scalation
       (assoc k value))))
  ([t3d axis value]
   (let [k  (keyword "scale" (name axis))
         ms (:matrix/scalation t3d)
         ss (cond
              (nil? ms) (+scale axis value)
              (nil? (k ms)) (assoc ms k value)
              :else (update ms k + value))]
     (assoc t3d :matrix/scalation ss))))




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
