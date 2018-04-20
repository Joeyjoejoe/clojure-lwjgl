(ns test-lwjgl.camera
  (:require [test-lwjgl.buffers :as buffer]
            [clojure.core.matrix :as m]
            [clojure.core.matrix.operators :as mo]))

(defn initialize
  ([] (let [position [0.0 10.0 10.0]
            front    [0.0 0.0 -1.0]
            target   [0.0 0.0 0.0]]
        (initialize position target front)))

  ([position target front] (let [direction (m/normalise (mo/- position target))
                                 right (m/normalise (m/cross [0.0 1.0 0.0] direction))
                                 up (m/cross direction right)]

        (atom {:position position
               :direction direction
               :front front
               :up up
               :acceleration {:forward false :backward false :left false :right false}}))))

(def camera (initialize))

(defn get-atom []
  camera)

(defn get-data []
 @camera)


