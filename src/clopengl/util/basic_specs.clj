(ns clopengl.util.basic-specs
  (:require [clojure.spec.alpha :as s]))


(s/def :clopengl/id int?)
(s/def :clopengl/path string?)
(s/def :clopengl/coord (s/nilable float?))

(s/def :all/float #(every? float? %))
(s/def :all/integer #(every? integer? %))

(s/def :vec/size.2 #(= 2 (count %)))
(s/def :vec/size.3 #(= 3 (count %)))
