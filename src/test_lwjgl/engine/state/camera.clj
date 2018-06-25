(ns test-lwjgl.engine.state.camera)

(defn new-motion
  ([]      (new-motion 0.5))
  ([speed] {:directions {:forward false :backward false :left false :right false}
            :speed      speed}))

(defn new-mouse [] {:mod       :camera ;; possible values: [:camera :normal]
                    :sensivity 0.05
                    :position  {:x 0.0 :y 0.0}})

(defn new-camera [] {:mod       :free
                     :position  [0.0 10.0 10.0]
                     :front     [0.0 0.0 -1.0]
                     :up        [0.0 1.0 0.0]
                     :angles    {:yaw -90.0 :pitch 0.0 :roll 0.0}
                     :motion    (new-motion)})

(def state (atom {:camera (new-camera)
                  :mouse  (new-mouse)
                  :deltatime 0.0
                  :fps {:value 0 :frames 0 :seconds 0}}))

(defn get-atom [] state)

(defn get-data
  ([] @state)
  ([k] (k @state)))
