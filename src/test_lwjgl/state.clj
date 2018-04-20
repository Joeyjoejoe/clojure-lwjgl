(ns test-lwjgl.state
  (:require [test-lwjgl.camera :as camera]))

(def state (atom {:camera nil
                  :deltatime 0.0
                  :camera-speed 10.0
                  :mouse-sensivity 0.05
                  :mouse-mod :camera
                  :mouse-position {:x 0.0 :y 0.0 :yaw -90.0 :pitch 0.0}}))

(defn get-atom [] state)

(defn get-data
  ([] @state)
  ([k] (k @state)))

(defn mouse-position
  ([] (get-data :mouse-position))
  ([k] (k (mouse-position))))

(defn camera-speed []
  (* (get-data :camera-speed) (get-data :deltatime)))
