(ns clopengl.matrices
  (:require [clopengl.data.math.3D-transformations :as data]
            [clopengl.interpret.math.3D-transformations :as _]
            [clopengl.data.math.camera-transformations :as ct]
            [clopengl.interpret.math.camera-transformations :as __]))


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
  ([[x y z]]
    (-> data/blank-translation
        (assoc :translate/x x)
        (assoc :translate/y y)
        (assoc :translate/z z)))
  ([axis value]
   (let [k (keyword "translate" (name axis))]
     (-> data/blank-translation
       (assoc k value))))
  ([t3d axis value]
   (let [t  (+translate axis value)
         tt (merge-with + (:matrix/translation t3d) t)]
     (assoc t3d :matrix/translation tt)))
  ([t3d x y z]
   (let [t (+translate [x y z])
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

(defn perspective [fovy aspect z-near z-far]
  (-> ct/blank-perspective
      (assoc :perspective/fovy fovy)
      (assoc :perspective/aspect aspect)
      (assoc :perspective/z-near z-near)
      (assoc :perspective/z-far z-far)))

(defn look-at [eye center up]
  (-> ct/blank-look-at
      (assoc :look-at/eye eye)
      (assoc :look-at/center center)
      (assoc :look-at/up up)))
