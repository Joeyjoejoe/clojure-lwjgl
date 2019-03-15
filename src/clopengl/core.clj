(ns clopengl.core
  (:use [clopengl.engine.utilities.misc])
  (:require [clopengl.engine.opengl.window :as window]
            [clopengl.engine.opengl.vertices :as vertices]
            [clopengl.engine.opengl.shaders.program :as program]
						[clopengl.engine.utilities.parser_3d.ply :as ply]
						[clopengl.engine.utilities.shapes.basic :as shape]
            [clopengl.engine.opengl.buffers :as buffer]
            [clopengl.engine.state.global :as state]
            [clopengl.engine.opengl.shaders.shader :as shader])
  (:import (org.lwjgl.glfw GLFW))
  (:gen-class))


(defn -main []
  "Start the game"

  (def window (window/create {:width 1280 :height 960 :title "My Game"}))

  (let [programs {:default (program/defprogram "vertices/default.vert" "fragments/lightnings/default.frag")
                  :light-source (program/defprogram "vertices/light-source.vert" "fragments/default.frag")}]
    (swap! (state/get-atom) assoc-in [:engine :shader-programs] programs))

  ;; Load shapes datas to GC and store render functions
  ;;(def pandaki (vertices/setup (ply/parse-ply "pandaki2.ply") 10 (state/shader-program :default)))
  ;;(def triangles (vertices/setup (shape/triangle true) 200 (state/shader-program :default)))
  ;;(def cubes (vertices/setup (shape/cube true) 100 (state/shader-program :default)))
  ;;(def ground (vertices/setup (shape/rectangle2D 100 75 :vertical) 1 (state/shader-program :default)))
  (def cube (vertices/setup (shape/cube-w-normals) 1 (state/shader-program :default)))
  (def lamp (vertices/setup (shape/cube true) 1 (state/shader-program :light-source)))

  (swap! (state/get-atom) assoc :render [cube lamp])

  ;;  Start game loop
  (loop [to-render-functions (state/get-data :render)
         curr (GLFW/glfwGetTime)
         prev (GLFW/glfwGetTime)
         lag (atom 0.0)]

    (swap! lag #(+ % (- curr prev)))
    (swap! (state/get-atom) assoc :deltatime (- curr prev))

    (clojure.pprint/pprint (state/get-data))
    (clojure.pprint/pprint curr)

    ;; Trigger events based on time
    ;; (time-triggers curr)

    ;;  (handle-inputs)
    (state/update-camera)
    ;; Handle game logic and update
    (while (>= @lag 0.1)
      ;;  (update)
      (swap! lag #(- % 0.1)))

    ;; (render (/ lag 0.1))
    (window/render window to-render-functions)

    ;; Calculate FPS
    (record-fps (state/get-atom))

    ;; Recur loop
    (if (not (GLFW/glfwWindowShouldClose window))
      (recur (state/get-data :render) (GLFW/glfwGetTime) curr lag)))

  ;; Quit game
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))
