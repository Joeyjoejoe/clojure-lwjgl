(ns clopengl.engine.state.global
  (:require [clopengl.engine.state.camera :as camera]
            [clopengl.engine.state.mouse :as mouse]))

(def state (atom {:window nil
                  :render []
                  :camera (camera/init)
                  :mouse  (mouse/init)
                  :deltatime 0.0
                  :fps {:value 0 :frames 0 :seconds 0}}))

(defn get-atom [] state)

(defn get-data
  ([] @state)
  ([k] (k @state)))

(defn update-camera []
  (camera/update (get-atom)))
