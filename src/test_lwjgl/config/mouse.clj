(ns test-lwjgl.config.mouse
  (:require [clojure.tools.logging :as log]
	          [clojure.core.matrix :as m]
	          [clojure.core.matrix.operators :as mo]
	          [test-lwjgl.state :as state]
            [test-lwjgl.camera :as camera])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback GLFWCursorPosCallback)
           (org.lwjgl.opengl GL11)))

(def normal-mouse (proxy [GLFWCursorPosCallback] []
  (invoke [win xpos ypos]
    (println (str "Mouse position: " xpos " " ypos)))))

(def fps-camera (proxy [GLFWCursorPosCallback] []
  (invoke [win xpos ypos]
    (let [sensivity (state/get-data :mouse-sensivity)
          last-x (state/mouse-position :x)
          last-y (state/mouse-position :y)
          x-offset (* sensivity (- xpos last-x))
          y-offset (* sensivity (- last-y ypos))
          yaw (+ x-offset (state/mouse-position :yaw))
          pitch (+ y-offset (state/mouse-position :pitch))
          pitch (if (< pitch -89.0) -89.0 pitch)
          pitch (if (> pitch 89.0) 89.0 pitch)
          pitch-cos (Math/cos (Math/toRadians pitch))
          front-x (* (Math/cos (Math/toRadians yaw)) pitch-cos)
          front-y (Math/sin (Math/toRadians pitch))
          front-z (* (Math/sin (Math/toRadians yaw)) pitch-cos)
          front (m/normalise [front-x front-y front-z])]
      (swap! (state/get-atom) assoc-in [:mouse-position] {:x xpos :y ypos :yaw yaw :pitch pitch})
      (swap! (camera/get-atom) assoc-in [:front] front)))))

(defn normal-mod [window]
  (do
    (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_NORMAL)
    (GLFW/glfwSetCursorPosCallback window normal-mouse)
    (swap! (state/get-atom) assoc-in [:mouse-mod] :normal)))

(defn camera-mod [window]
  (let [state-atom (state/get-atom)
        position (:mouse-position @state-atom)]
    (do
      (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_DISABLED)
      (GLFW/glfwSetCursorPos window (:x position) (:y position))
      (GLFW/glfwSetCursorPosCallback window fps-camera)
      (swap! state-atom assoc-in [:mouse-mod] :camera))))

(defn toggle-mouse-mod []
  (fn [window]
    (if (= (state/get-data :mouse-mod) :camera) (normal-mod window) (camera-mod window))))

