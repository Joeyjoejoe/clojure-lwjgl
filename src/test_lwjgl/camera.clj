(ns test-lwjgl.camera
  (:require [test-lwjgl.buffers :as buffer]
            [clojure.core.matrix :as m]))

(defn initialize
  ([] (let [position [0.0 10.0 10.0]
            front [0.0 0.0 -1.0]
            target [0.0 0.0 0.0]]
        (initialize position target front)))

  ([position target front] (let [target (m/array target)
                           direction (m/normalise position)
                           ;; direction (m/normalise (m/sub position target)) ;; FPS
                           right (m/normalise (m/cross (m/array [0.0 1.0 0.0]) direction))
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


