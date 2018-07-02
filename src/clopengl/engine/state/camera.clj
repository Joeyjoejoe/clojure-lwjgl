(ns clopengl.engine.state.camera
  (:require [clojure.core.matrix :as m]
            [clojure.core.matrix.operators :as mo]))

(defn new-motion
  ([]      (new-motion 0.5))
  ([speed] {:directions {:forward false :backward false :left false :right false}
            :speed      speed}))

(defn init [] {:mod  :free
                     :position  [0.0 10.0 10.0]
                     :front     [0.0 0.0 -1.0]
                     :up        [0.0 1.0 0.0]
                     :angles    {:yaw -90.0 :pitch 0.0 :roll 0.0}
                     :motion    (new-motion)})

(defn update [state]
  (let [gstate     state
        camera     (:camera @gstate)
        front      (:front    camera)
        up         (:up       camera)
        position   (:position camera)
        directions (get-in camera [:motion :directions])
        speed      (get-in camera [:motion :speed])
        no-change  [0.0 0.0 0.0]
        front-up   (m/normalise (m/cross front up))
        forward    (if (:forward directions)  (mo/* speed front)         no-change)
        backward   (if (:backward directions) (mo/* speed front -1.0)    no-change)
        left       (if (:left directions)     (mo/* speed front-up -1.0) no-change)
        right      (if (:right directions)    (mo/* speed front-up)      no-change)]
    (swap! gstate assoc-in [:camera :position] (mo/+ position forward right left backward))))

