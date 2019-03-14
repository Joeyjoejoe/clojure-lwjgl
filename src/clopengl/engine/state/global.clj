(ns clopengl.engine.state.global
  (:require [clopengl.engine.state.camera :as camera]
            [clopengl.engine.state.engine :as engine]
            [clopengl.engine.state.mouse :as mouse]))

(def state (atom {:window nil
                  :render []
                  :camera (camera/init)
                  :mouse  (mouse/init)
                  :engine (engine/init)
                  :deltatime 0.0
                  :fps {:value 0 :frames 0 :seconds 0}}))

(defn get-atom [] state)

(defn get-data
  ([] @state)
  ([k] (k @state)))

(defn update-camera []
  (camera/update (get-atom)))

(defn shader-program [identifier]
  (-> (get-data :engine) :shader-programs identifier))
