(ns test-lwjgl.utility)

;; Picked from here: http://blog.jayfields.com/2011/01/clojure-select-keys-select-values-and.html
(defn select-values [map ks]
  "Returns values of keys in order as a vector"
  (reduce #(conj %1 (map %2)) [] ks))

(defn randcc [n]
  "Returns randomly 1.0 or 0.0"
  (float (rand-int n)))

(defn map-key-values [m f key]
  (into {} (for [[k v] m] 
    (cond 
      (= k key) [k (f v)]
      :else [k v]))))

