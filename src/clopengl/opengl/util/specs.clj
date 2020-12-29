(ns clopengl.opengl.util.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.java.io :as io]))

(s/def :clopengl/asset #(some? (io/resource %)))
(s/def :clopengl/path (s/and string? :clopengl/asset))

(defn test-file
  [path]
  (s/explain-data :clopengl/path path))

(test-file "foo")
