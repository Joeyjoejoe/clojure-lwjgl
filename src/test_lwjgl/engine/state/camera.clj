(ns test-lwjgl.engine.state.camera)

(defn new-motion
  ([]      (new-motion 0.5))
  ([speed] {:directions {:forward false :backward false :left false :right false}
            :speed      speed}))

(defn init [] {:mod       :free
                     :position  [0.0 10.0 10.0]
                     :front     [0.0 0.0 -1.0]
                     :up        [0.0 1.0 0.0]
                     :angles    {:yaw -90.0 :pitch 0.0 :roll 0.0}
                     :motion    (new-motion)})
