(ns test-lwjgl.engine.state.global
  (:require [test-lwjgl.engine.state.camera :as camera]
            [test-lwjgl.engine.state.mouse :as mouse]))

(def state (atom {:camera (camera/init)
                  :mouse  (mouse/init)
                  :deltatime 0.0
                  :fps {:value 0 :frames 0 :seconds 0}}))

(defn get-atom [] state)

(defn get-data
  ([] @state)
  ([k] (k @state)))
