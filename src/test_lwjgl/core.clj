(ns test-lwjgl.core
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.window :as window]
						[clojure.java.io :as io]
						[test-lwjgl.parser_3d.ply :as ply]
						[test-lwjgl.shapes.basic :as shape]
            [clojure.core.matrix :as m]
            [clojure.core.matrix.operators :as mo]
            [test-lwjgl.buffers :as buffer]
            [test-lwjgl.state :as state]
            [test-lwjgl.transformations :as transformation]
            [test-lwjgl.shader :as shader]
            [clojure.tools.logging :as log])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl BufferUtils)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL20 GLCapabilities GL))
  (:gen-class))


(defn -main []
  "Start the game"

  (def window (window/create {:width 1280 :height 960 :title "My Shitty Game"}))
  (shader/init-defaults)

  ;; Load shapes datas to GC and store render functions
  (def pandaki (window/vertex-setup (ply/parse-ply "pandaki2.ply") 10))
  (def triangles (window/vertex-setup (shape/triangle true) 200))
  (def cubes (window/vertex-setup (shape/cube true) 100))
  (def ground (window/vertex-setup (shape/rectangle 100 75) 1))

  (let [mouse-position (window/get-size window)
        x (/ (:width mouse-position) 2.0)
        y (/ (:height mouse-position) 2.0)]
  (swap! (state/get-atom) assoc-in [:mouse :position] {:x x :y y}))
  ;;  Start game loop
  (loop [to-render-functions [ground pandaki]
         curr (GLFW/glfwGetTime)
         prev (GLFW/glfwGetTime)
         lag (atom 0.0)]

    (swap! lag #(+ % (- curr prev)))
    (swap! (state/get-atom) assoc :deltatime (- curr prev))

    (clojure.pprint/pprint (state/get-data))
    ;;  (handle-inputs)
    (let [gstate      (state/get-atom)
          camera     (state/get-data :camera)
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
      (swap! gstate assoc-in [:camera :position] (mo/+ position forward right left backward)))

    ;; Handle game logic and update
    (while (>= @lag 0.1)
      ;;  (update)
      (swap! lag #(- % 0.1)))

    ;; (render (/ lag 0.1))
    (window/render window to-render-functions)

    ;; Calculate FPS
    (let [state  (state/get-atom)
          fps    (state/get-data :fps)
          frames   (:frames fps)
          seconds (:seconds fps)]
      (if (>= (- (GLFW/glfwGetTime) seconds) 1.0)
            (swap! state assoc :fps {:value (+ 1 frames) :frames 0 :seconds (inc seconds)})
            (swap! state assoc-in [:fps :frames] (inc frames))))

    ;; Recur loop
    (if (not (GLFW/glfwWindowShouldClose window))
      (recur to-render-functions (GLFW/glfwGetTime) curr lag)))

  ;; Quit game
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))
