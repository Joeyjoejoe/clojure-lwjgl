(ns test-lwjgl.engine.state.mouse)

(defn init [] {:mod       :camera ;; possible values: [:camera :normal]
                    :sensivity 0.05
                    :position  {:x 0.0 :y 0.0}})

