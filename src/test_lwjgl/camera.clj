(ns test-lwjgl.camera
  (:require [test-lwjgl.buffers :as buffer]
            [clojure.core.matrix :as m]))

(defn initialize 
  ([] (let [position [0.0 0.0 3.0]
            front [0.0 0.0 -1.0]
            target [0.0 0.0 0.0]]
        (initialize position target front)))

  ([position target front] (let [target (m/array target)
                           direction (m/normalise (m/sub position target))
                           right (m/normalise (m/cross (m/array [0.0 1.0 0.0]) direction))
                           up (m/cross direction right)]

        (atom {:position position
               :direction direction
               :front front
               :up up}))))

(def camera (initialize))
(println camera)

(defn get-atom []
  camera)

(defn get-raw []
 @camera)


