(ns clopengl.util.basic-specs
  (:require [clojure.spec.alpha :as s]))


(s/def :clopengl/id int?)
(s/def :clopengl/path string?)
(s/def :clopengl/coord (s/nilable float?))

(s/def :float/vec3 (s/and #(= 3 (count %))
                          #(every? float? %)))
;; (s/def :clgl/id int?)
;; (s/def :clgl/path string?)
;; (s/def :clgl/coord (s/nilable float?))
;;
;; (s/def :vec3d (s/and vector? #(= 3 (count %))))
;; (s/def :vec3d/float (s/and :vec3d #(every? float? %)))
;; (s/def :vec3d/integer(s/and :vec3d #(every? integer? %)))
;;
;; (s/def :vec2d (s/and vector? #(= 2 (count %))))
;; (s/def :vec2d/float (s/and :vec2d #(every? float? %)))
;; (s/def :vec2d/integer(s/and :vec2d #(every? integer? %)))
;;
;;
;; (s/def :color/rgb (s/and :vec3d #(every? #(<= 0 % 1) %)))
