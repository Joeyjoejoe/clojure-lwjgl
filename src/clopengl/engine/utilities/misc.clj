(ns clopengl.engine.utilities.misc
  (:require [clopengl.engine.utilities.transformations :as transformation]))

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

(defmacro doseq-indexed
  "loops over a set of values, binding index-sym to the 0-based index of each value"
  ([[val-sym values index-sym] & code]
  `(loop [vals# (seq ~values)
          ~index-sym (long 0)]
     (if vals#
       (let [~val-sym (first vals#)]
             ~@code
             (recur (next vals#) (inc ~index-sym)))
		nil))))

(defn rand-coordinates [n min max]
  (map (fn [x] (vector (+ (rand min) (rand max)) (+ (rand min) (rand max)) (+ (rand min) (rand max)))) (vec (repeat n nil))))

(defn rand-positions
  ([n] (rand-positions n -10 10))
  ([n min max] (map #(transformation/make "translate-matrix" %) (rand-coordinates n min max))))

