(ns test-lwjgl.state
  (:require [test-lwjgl.camera :as camera]))

(def state (atom {:camera nil 
                  :deltatime 0.0 
                  :camera-speed 0.5}))

(defn get-atom [] state)
(defn get-data [] @state)

(defn camera-speed []
  (* 1.0 (:deltatime @state)))
