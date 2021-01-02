(ns clopengl.opengl.data.space-mutations
  ""
  )

(defn transform-matrix
  "Create a transform"
  []
  {:type :transform/matrix
   :rotation {:x 1.0 :y 1.0 :z 1.0}
   :translation {:x 0 :y 0 :z 0}
   :scale 1.0})

(defn +rotate
  ([axis angle]
   {axis angle})
  ([motion axis angle]
   (let [r (+rotate axis angle)]
     (assoc motion :rotation (merge (:rotation motion) r)))))

(defn +translate
  ([axis value]
   {axis value})
  ([motion axis value]
   (let [t (+translate axis value)]
     (assoc motion :translation (merge (:translation motion) t)))))

(defn +scale
  ([motion value]
   (assoc motion :scale value)))
