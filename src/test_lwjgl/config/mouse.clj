(ns test-lwjgl.config.mouse
  (:require [clojure.tools.logging :as log]
	          [clojure.core.matrix :as m]
	          [clojure.core.matrix.operators :as mo]
	          [test-lwjgl.state :as state])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback GLFWCursorPosCallback)
           (org.lwjgl.opengl GL11)))

(def normal-mouse (proxy [GLFWCursorPosCallback] []
  (invoke [win xpos ypos]
    (println (str "Mouse position: " xpos " " ypos)))))

(def fps-camera (proxy [GLFWCursorPosCallback] []
  (invoke [win xpos ypos]
    (let [gstate    (state/get-atom)
          mouse     (state/get-data :mouse)
          camera    (state/get-data :camera)
          sensivity (:sensivity mouse)
          last-x    (get-in mouse [:position :x])
          last-y    (get-in mouse [:position :y])
          x-offset  (* sensivity (- xpos last-x))
          y-offset  (* sensivity (- last-y ypos))
          yaw       (get-in camera [:angles :yaw])
          pitch     (get-in camera [:angles :pitch])
          yaw       (+ x-offset yaw)
          pitch     (+ y-offset pitch)
          pitch     (if (< -89.0 pitch 89.0) pitch (if (neg? pitch) -89.0 89.0))
          pitch-cos (Math/cos (Math/toRadians pitch))
          front-x   (* (Math/cos (Math/toRadians yaw)) pitch-cos)
          front-y   (Math/sin (Math/toRadians pitch))
          front-z   (Math/sin (Math/toRadians yaw))
          front     (m/normalise [front-x front-y front-z])]
      (swap! gstate assoc-in [:mouse :position] {:x xpos :y ypos})
      (swap! gstate assoc-in [:camera :angles] {:yaw yaw :pitch pitch :roll 0.0})
      (swap! gstate assoc-in [:camera :front] front)))))

(defn normal-mod [window]
  (do
    (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_NORMAL)
    (GLFW/glfwSetCursorPosCallback window normal-mouse)
    (swap! (state/get-atom) assoc-in [:mouse :mod] :normal)))

(defn camera-mod [window]
  (let [gstate    (state/get-atom)
        mouse    (state/get-data :mouse)
        position (:position mouse)]
    (do
      (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_DISABLED)
      (GLFW/glfwSetCursorPos window (:x position) (:y position))
      (GLFW/glfwSetCursorPosCallback window fps-camera)
      (swap! gstate assoc-in [:mouse :mod] :camera))))

(defn toggle-mouse-mod []
  (fn [window]
    (let [mouse (state/get-data :mouse)]
      (if (= (:mod mouse) :camera) (normal-mod window) (camera-mod window)))))

