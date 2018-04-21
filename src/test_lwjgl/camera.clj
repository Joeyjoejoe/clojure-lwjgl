(ns test-lwjgl.camera
  (:require [test-lwjgl.buffers :as buffer]
            [clojure.core.matrix :as m]
            [clojure.core.matrix.operators :as mo]))

(defn initialize []
  (atom {:position [0.0 10.0 10.0]
         :front [0.0 0.0 -1.0]
         :up [0.0 1.0 0.0]
         :acceleration {:forward false :backward false :left false :right false}}))

(def camera (initialize))

(defn get-atom []
  camera)

(defn get-data []
 @camera)


